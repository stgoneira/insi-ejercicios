using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using EstacionamientoEIP;

namespace UnitTestProject
{
    [TestClass]
    public class XmlaCanonicoTestClass
    {
        [TestMethod]
        public void TestXMLaCanonico()
        {
            var xml = @"
<acceso>
    <fecha>2025-01-08T08:30:01-03:00</fecha>
    <patente>XCRB98</patente>
    <porton>norte-01</porton>
</acceso>".Trim();

            var traductor = new TraductorCanonico(xml, "1");
            var resultado = traductor.Ejecutar();
            Console.WriteLine($"Resultado: {resultado}");
            var esperado = @"{""acceso"":{""fecha"":""\/Date(1736335801000-0300)\/"",""porton"":""norte-01""},""eventoId"":""1"",""sujeto"":{""categoria"":""Desconocido"",""nivelAcceso"":""Desconocido"",""nombre"":""Desconocido""},""vehiculo"":{""color"":""Desconocido"",""modelo"":""Desconocido"",""patente"":""XCRB98""}}";
            Console.WriteLine($"Esperado: {esperado}");
            Assert.AreEqual(esperado, resultado);
        }
    }
}
