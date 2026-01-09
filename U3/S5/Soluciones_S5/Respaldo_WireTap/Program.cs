using System;
using System.Data.SqlClient;
using System.Messaging;
using System.Threading;
using System.Threading.Tasks;

namespace Respaldo_WireTap
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var cola = new MessageQueue(@".\private$\accesos_raw") { 
                Formatter = new XmlMessageFormatter(new[] { typeof(string) }) 
            }; 
            
            // Cancela ordenadamente al presionar Enter
            var cts = new CancellationTokenSource(); 
            
            // Ejecuta el bucle receptor en segundo plano
            Task.Run(() => RecibirContinuamenteTransaccional(cola, cts.Token)); 
            Console.WriteLine("Escuchando mensajes de manera continua y transaccional en " + cola.Path); 
            Console.WriteLine("Presiona Enter para salir..."); 
            Console.ReadLine(); 
            cts.Cancel();
        }

        static void RecibirContinuamenteTransaccional(MessageQueue cola, CancellationToken ct)
        {
            while (!ct.IsCancellationRequested)
            {
                using (var tx = new MessageQueueTransaction())
                {
                    try
                    {
                        tx.Begin(); 
                        
                        // Bloquea hasta recibir un mensaje
                        var msg = cola.Receive(tx);
                        // Procesar el contenido
                        var contenido = msg.Body as string;

                        Console.WriteLine("Mensaje recibido: " + contenido);
                        InsertarEnBD(contenido);

                        // Confirma la transacción
                        tx.Commit();
                    }
                    catch (MessageQueueException mqe)
                    { // Errores típicos de MSMQ (timeouts, etc.)
                        tx.Abort();
                        Console.Error.WriteLine("MQ error: " + mqe.Message);
                    }
                    catch (Exception ex)
                    {
                        tx.Abort();
                        Console.Error.WriteLine("Error procesando mensaje: " + ex.Message);
                    }
                }
            }
        }

         static void InsertarEnBD(string accesoXML)
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
                    Console.WriteLine($"Se insertaron {filas} registro(s) en la tabla Accesos_backup");
                }
            }
        }
    }
}
