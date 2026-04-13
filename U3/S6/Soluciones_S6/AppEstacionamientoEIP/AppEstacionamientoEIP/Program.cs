using EstacionamientoEIP;
using EstacionamientoEIP.Enrutador;
using System;
using System.Diagnostics;

namespace AppEstacionamientoEIP
{
    internal class Program
    {
        public string XmlBasePath { get; set; }     = @"Z:\AccesosXML";
        public string ColaAccesosRaw { get; set; }  = "accesos_raw";
        public string ColaAccesos { get; set; }     = "accesos";
        public string ColaAccesosSospechosos { get; set; } = "accesos_sospechosos";
        public string ColaAccesosNormales { get; set; } = "accesos_normales";


        static void Main(string[] args)
        {
           new Program().Ejecutar();                
        }

        public void Ejecutar()
        {
            Trace.Listeners.Add(new ConsoleTraceListener());
            Trace.WriteLine("Program.Ejecutar()", "INFO");

            // 1. XML Adapter 
            // crea un listener sobre carpeta con XMLs
            // y envía cada acceso a MSMQ accesos_raw
            new AccesoXMLAdapter(
                XmlBasePath,
                ColaAccesosRaw
                ).Ejecutar();

            // 2. WireTap 
            // escucha mensajes cola accesos_raw
            // y respalda en una BD 
            var wireTap = new AccesoWireTap(ColaAccesosRaw);
            wireTap.OnMensajeRespaldado += (sender, args) => 
            {
                Trace.WriteLine("wireTap.OnMensajeRespaldado", "INFO");
                var mensajeRaw = args.Mensaje;

                // 3. Traductor
                // traduce mensaje XML a JSON canónico 
                var mensajeJsonCanonico = new TraductorCanonico(mensajeRaw, Guid.NewGuid().ToString()).Ejecutar();
                Trace.WriteLine($"Json Canonico: {mensajeJsonCanonico}", "INFO");

                // 4. Enriquecimiento 
                // enriquece el mensaje canónico 
                // con datos del vehiculo y persona 
                // que probablemente hizo el ingreso 
                var mensajeEnriquecido = new Enriquecedor(mensajeJsonCanonico, "accesos").Ejecutar();
            };
            wireTap.Ejecutar(); 
            
            // 5. Enrutador 
            // evalúa si el acceso es sospechoso o no
            // y envía a la cola correspondiente
            new Enrutador(
                ColaAccesos                
                ,ColaAccesosNormales
                ,ColaAccesosSospechosos
            ).Ejecutar();

            // 6. Bridge 
            // el Bridge se implementará con un Trigger
            // por lo que no se agrega en esta app

            Console.ReadLine();// mantine la app abierta
        }
    }
}
