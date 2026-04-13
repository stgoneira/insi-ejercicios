using System;
using System.Data.SqlClient;
using System.Diagnostics;
using System.Messaging;

namespace EstacionamientoEIP
{
    internal class AccesoWireTap
    {
        private string colaAccesosRaw;
        public event EventHandler<MensajeRespaldadoEventArgs> OnMensajeRespaldado;
        public class MensajeRespaldadoEventArgs : EventArgs
        {
            public string Mensaje { get; set; }
        }

        public AccesoWireTap(string colaAccesosRaw)
        {
            this.colaAccesosRaw = colaAccesosRaw;
        }

        public void Ejecutar()
        {
            Trace.WriteLine("AccesoWireTap.Ejecutar()", "INFO");
            var cola = new MessageQueue($@".\private$\{colaAccesosRaw}");
            cola.Formatter = new XmlMessageFormatter(new[] { typeof(string) });
            cola.ReceiveCompleted += (sender, args) =>
            {
                Trace.WriteLine("AccesoWireTap.Ejecutar().ReceiveCompleted", "INFO");
                var mq = (MessageQueue)sender;
                using (var tx = new MessageQueueTransaction())
                {
                    tx.Begin();
                    try
                    {
                        var mensaje = mq.EndReceive(args.AsyncResult);
                        var cuerpo = (string)mensaje.Body;
                        insertarEnBD(cuerpo);
                        var eventArgs = new MensajeRespaldadoEventArgs()
                        {
                            Mensaje = cuerpo
                        };
                        OnMensajeRespaldado.Invoke(this, eventArgs);
                        tx.Commit();
                        mq.BeginReceive();
                    }
                    catch (Exception ex)
                    {
                        {
                            Trace.TraceError(ex.Message);
                            tx.Abort();
                        }
                    }
                    Trace.WriteLine("AccesoWireTap.Ejecutar().ReceiveCompleted END", "INFO");
                }                                
            };
            cola.BeginReceive();
        }

        

        private void insertarEnBD(string accesoXML)
        {
            // Cadena de conexión a LocalDB
            string strConexion = "Server=(localdb)\\MSSQLLocalDB;Integrated Security=true;Database=EstacionamientoDB;";

            using (SqlConnection conexion = new SqlConnection(strConexion))
            {
                conexion.Open();
                Console.WriteLine("Conexión establecida con SQL Server...");

                // Crear tabla solo si no existe
                string sqlCreateTable = @"
                    IF OBJECT_ID('dbo.Accesos_backup', 'U') IS NULL
                    BEGIN
                        CREATE TABLE dbo.Accesos_backup (
                            id BIGINT NOT NULL IDENTITY PRIMARY KEY,
                            payload NVARCHAR(MAX) NOT NULL,
                            created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
                        );
                    END";

                using (SqlCommand cmdCreate = new SqlCommand(sqlCreateTable, conexion))
                {
                    cmdCreate.ExecuteNonQuery();
                    Console.WriteLine("Tabla creada correctamente ...");
                }

                // Insertar un registro
                string sqlInsert = @"
                    INSERT INTO dbo.Accesos_backup (payload) 
                    VALUES (@payload);";

                using (SqlCommand cmdInsert = new SqlCommand(sqlInsert, conexion))
                {
                    cmdInsert.Parameters.AddWithValue("@payload", accesoXML);
                    int filas = cmdInsert.ExecuteNonQuery();
                    Trace.WriteLine($"Se insertaron {filas} registro(s) en la tabla Accesos_backup");
                }
            }
        }


    }
}