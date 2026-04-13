using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;

namespace EstacionamientoEIP
{
    public class TraductorCanonico
    {
        private string _mensajeXml = string.Empty;
        private string _eventoId = string.Empty;
        public TraductorCanonico(string mensajeXml, string eventoId) 
        { 
            _mensajeXml = mensajeXml;
            _eventoId = eventoId;
        }

        static string DESCONOCIDO = "Desconocido";
        public string Ejecutar()
        {
            Trace.WriteLine("TraductorCanonico.Ejecutar()", "INFO");
            var serializer = new DataContractSerializer(typeof(AccesoRaw));
            AccesoRaw accesoRaw = null;
            using (var stream = new MemoryStream(Encoding.UTF8.GetBytes(_mensajeXml)))
            {
                accesoRaw = (AccesoRaw)serializer.ReadObject(stream);
            }
            var accesoCanonico = new AccesoCanonico
            {
                EventoId = _eventoId,
                Vehiculo = new Vehiculo()
                {
                    Patente = accesoRaw.Patente,
                    Modelo = DESCONOCIDO,
                    Color = DESCONOCIDO
                },
                Acceso = new Acceso()
                {
                    Porton = accesoRaw.Porton,
                    Fecha = accesoRaw.Fecha,
                },
                Sujeto = new Sujeto()
                {
                    Nombre = DESCONOCIDO,
                    Categoria = DESCONOCIDO,
                    NivelAcceso = DESCONOCIDO
                }
            };

            return generarJson(accesoCanonico);
        }

        private string generarJson(AccesoCanonico accesoCanonico)
        {
            var serializer = new DataContractJsonSerializer(typeof(AccesoCanonico));
            using (var stream = new MemoryStream())
            {
                serializer.WriteObject(stream, accesoCanonico);
                return Encoding.UTF8.GetString(stream.ToArray());
            }
        }

        [DataContract(Namespace = "", Name = "acceso")]
        private class AccesoRaw
        {
            [DataMember(Name = "fecha")]
            public DateTime Fecha { get; set; }

            [DataMember(Name = "patente")]
            public string Patente { get; set; }

            [DataMember(Name = "porton")]
            public string Porton { get; set; }
        }

        [DataContract]
        private class AccesoCanonico
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
        private class Vehiculo
        {
            [DataMember(Name = "patente")]
            public string Patente { get; set; }

            [DataMember(Name = "modelo")]
            public string Modelo { get; set; }

            [DataMember(Name = "color")]
            public string Color { get; set; }
        }

        [DataContract]
        private class Acceso
        {
            [DataMember(Name = "porton")]
            public string Porton { get; set; }

            [DataMember(Name = "fecha")]
            public DateTime Fecha { get; set; }
        }

        [DataContract]
        private class Sujeto
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
