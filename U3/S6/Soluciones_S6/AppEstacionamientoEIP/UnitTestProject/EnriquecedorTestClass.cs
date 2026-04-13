using EstacionamientoEIP;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;

namespace UnitTestProject
{
    [TestClass]
    public class EnriquecedorTest
    {
        
        [TestMethod]
        public void TestJsonInfo2Obj()
        {
            var json = @"{""patente"":""BEBE99"",""modelo"":""Suzuki Swift"",""color"":""Rojo"",""nombreSujeto"":""Ana María Soto"",""categoria"":""Empleado"",""nivelAcceso"":""Medio""}";
            var mensajeJsonCanonico = "";
            var colaCanonica = "";
            var enriquecedor = new Enriquecedor(mensajeJsonCanonico, colaCanonica);
            var obj = enriquecedor.JsonInfo2Object(json);
            Assert.AreEqual("Suzuki Swift", obj.Modelo);
            Assert.AreEqual("Medio", obj.NivelAcceso);
            Assert.AreEqual("Empleado", obj.Categoria);
            Assert.AreEqual("Ana María Soto", obj.NombreSujeto);
        }
    }
}
