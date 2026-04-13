using System;
using System.Diagnostics;
using System.IO;
using System.Messaging;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;

namespace EstacionamientoEIP.Enrutador
{
    public class Enrutador
    {
        private string _colaCanonica;
        private string _colaNormal;
        private string _colaSospechosos;
        public Enrutador(string colaCanonica, string colaNormal, string colaSospechosos)
        {
            _colaCanonica = colaCanonica;
            _colaNormal = colaNormal;
            _colaSospechosos = colaSospechosos;
        }

        public void Ejecutar()
        {
            Trace.WriteLine("Enrutador.Ejecutar()", "INFO");
            var cola = new MessageQueue($@".\private$\{_colaCanonica}");
            cola.Formatter = new ActiveXMessageFormatter();
            cola.ReceiveCompleted += (sender, args) =>
            {
                var mq = (MessageQueue)sender;
                using (var tx = new MessageQueueTransaction())
                {
                    tx.Begin();
                    try
                    {
                        var mensaje = mq.EndReceive(args.AsyncResult);
                        var cuerpo = (string)mensaje.Body;

                        // 1. Convertir a objeto
                        Trace.WriteLine(cuerpo, "INFO");
                        var obj = Json2Canonico(cuerpo);

                        // 2. Clasificar 
                        var clasificacion = ClasificarAcceso(obj);

                        // 3. Enviar a cola correspondiente
                        Trace.WriteLine($"Enviando mensaje clasificado como: {clasificacion.ToString()}", "INFO");
                        enviarMensaje(cuerpo, clasificacion);

                        tx.Commit();
                        mq.BeginReceive();
                    }
                    catch (Exception ex)
                    {
                        {
                            Trace.WriteLine($"Error en Enrutador: {ex.Message}", "ERROR");
                            tx.Abort();
                        }
                    }
                }                                
            };
            cola.BeginReceive();
        }


        public ClasificacionAcceso ClasificarAcceso(AccesoCanonico accesoCanonico)
        {
            if (accesoCanonico == null || accesoCanonico.Sujeto == null || accesoCanonico.Acceso == null)
            {
                return ClasificacionAcceso.Sospechoso;
            }

            string categoria = accesoCanonico.Sujeto.Categoria?.ToLowerInvariant();
            DateTime fecha = accesoCanonico.Acceso.Fecha;
            TimeSpan hora = fecha.TimeOfDay;

            switch (categoria)
            {
                case "empleado":
                    if (hora >= TimeSpan.FromHours(7) && hora <= TimeSpan.FromHours(20))
                    {
                        return ClasificacionAcceso.Normal;
                    }
                    break;

                case "proveedor":
                    if (hora >= TimeSpan.FromHours(8) && hora <= TimeSpan.FromHours(14))
                    {
                        return ClasificacionAcceso.Normal;
                    }
                    break;

                default:
                    // Categoría desconocida
                    return ClasificacionAcceso.Sospechoso;
            }

            // Si no cumple los rangos horarios
            return ClasificacionAcceso.Sospechoso;
        }

        private void enviarMensaje(string contenidoJson, ClasificacionAcceso clasificacion)
        {
            string colaNombre = clasificacion == ClasificacionAcceso.Normal ? _colaNormal : _colaSospechosos;
            using (var tx = new MessageQueueTransaction())
            {
                try
                {
                    tx.Begin();
                    var cola = new MessageQueue($@".\private$\{colaNombre}");
                    var formatter = new ActiveXMessageFormatter();

                    var mensaje = new Message(contenidoJson, formatter)
                    {
                        Recoverable = true
                    };

                    cola.Send(mensaje, tx);
                    tx.Commit();
                }
                catch (Exception e)
                {
                    tx.Abort();
                    Console.Error.WriteLine($"Error inesperado, no se pudo enviar el mensaje a la cola {_colaCanonica}: ", e.Message);
                }
            }
        }

        private string Canonico2Json(AccesoCanonico accesoCanonico)
        {
            var serializer = new DataContractJsonSerializer(typeof(AccesoCanonico));
            using (var stream = new MemoryStream())
            {
                serializer.WriteObject(stream, accesoCanonico);
                return Encoding.UTF8.GetString(stream.ToArray());
            }
        }


        public AccesoCanonico Json2Canonico(string json)
        {
            var serializer = new DataContractJsonSerializer(typeof(AccesoCanonico));
            using (var stream = new MemoryStream(Encoding.UTF8.GetBytes(json)))
            {
                var objCanonico = (AccesoCanonico)serializer.ReadObject(stream);
                return objCanonico;
            }
        }



    }
    public enum ClasificacionAcceso
    {
        Normal,
        Sospechoso
    }

    [DataContract]
    public class AccesoCanonico
    {
        [DataMember(Name = "eventoId")]
        public string EventoId { get; set; }
        [DataMember(Name = "vehiculo")]
        public Vehiculo Vehiculo { get; set; }
        [DataMember(Name = "acceso")]
        public Acceso Acceso { get; set; }
        [DataMember(Name = "sujeto")]
        public Sujeto Sujeto { get; set; }
    }

    [DataContract]
    public class Vehiculo
    {
        [DataMember(Name = "patente")]
        public string Patente { get; set; }

        [DataMember(Name = "modelo")]
        public string Modelo { get; set; }

        [DataMember(Name = "color")]
        public string Color { get; set; }
    }

    [DataContract]
    public class Acceso
    {
        [DataMember(Name = "porton")]
        public string Porton { get; set; }

        [DataMember(Name = "fecha")]
        public DateTime Fecha { get; set; }
    }

    [DataContract]
    public class Sujeto
    {
        [DataMember(Name = "nombre")]
        public string Nombre { get; set; }

        [DataMember(Name = "categoria")]
        public string Categoria { get; set; }

        [DataMember(Name = "nivelAcceso")]
        public string NivelAcceso { get; set; }
    }
}
