using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Messaging;
using System.Net.Http;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;

namespace EstacionamientoEIP
{
    public class Enriquecedor
    {
        private string _mensajeJsonCanonico;
        private string _colaCanonica;

        public Enriquecedor(string mensajeJsonCanonico, string colaCanonica)
        {
            this._mensajeJsonCanonico = mensajeJsonCanonico;
            this._colaCanonica = colaCanonica;
        }

        public string Ejecutar()
        {
            Trace.WriteLine("Enriquecedor.Ejecutar()", "INFO");
            var objCanonico     = Json2Canonico(_mensajeJsonCanonico);
            var infoJson        = GetInfoPorPatente(objCanonico.Vehiculo.Patente).Result;
            var info            = JsonInfo2Object(infoJson);
            var objEnriquecido  = Enriquecer(info, objCanonico);
            var jsonCanonicoEnriquecido = Canonico2Json(objEnriquecido);
            enviarMensaje(jsonCanonicoEnriquecido);
            return jsonCanonicoEnriquecido;
        }

        private void enviarMensaje(string contenidoJson)
        {
            using (var tx = new MessageQueueTransaction())
            {
                try
                {
                    tx.Begin();
                    var cola = new MessageQueue($@".\private$\{_colaCanonica}");
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

        public AccesoCanonico Enriquecer(InfoPorPatente info, AccesoCanonico objCanonico)
        {
            objCanonico.Vehiculo.Color = info.Color;
            objCanonico.Vehiculo.Modelo = info.Modelo;

            objCanonico.Sujeto.Categoria = info.Categoria;
            objCanonico.Sujeto.Nombre = info.NombreSujeto;
            objCanonico.Sujeto.NivelAcceso = info.NivelAcceso;
            return objCanonico;
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

        public async Task<string> GetInfoPorPatente(string patente)
        {
            var url = $"http://localhost:5000/api/info/vehiculo/{patente}";
            using (var cliente = new HttpClient())
            {
                return await cliente.GetStringAsync(url);
            }
        }

        public InfoPorPatente JsonInfo2Object(string json)
        {
            var serializer = new DataContractJsonSerializer(typeof(InfoPorPatente));
            using(var stream = new MemoryStream(Encoding.UTF8.GetBytes(json)))
            {
                return (InfoPorPatente)serializer.ReadObject(stream);
            }
        }

        [DataContract]
        public class InfoPorPatente
        {
            [DataMember(Name = "patente")]
            public string Patente { get; set; }
            [DataMember(Name = "modelo")]
            public string Modelo { get; set; }
            [DataMember(Name = "color")]
            public string Color { get; set; }
            [DataMember(Name = "nombreSujeto")]
            public string NombreSujeto { get; set; }
            [DataMember(Name = "categoria")]
            public string Categoria { get; set; }
            [DataMember(Name = "nivelAcceso")]
            public string NivelAcceso { get; set; }
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
}