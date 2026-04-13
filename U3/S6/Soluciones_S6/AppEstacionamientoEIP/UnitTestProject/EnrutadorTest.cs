using EstacionamientoEIP.Enrutador;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;

namespace UnitTestProject
{
    [TestClass]
    public class EnrutadorTest
    {
        private string _colaCanonica = "";
        [TestMethod]
        public void TestClasificarAcceso_EmpleadoDentroHorario()
        {
            var accesoCanonico = new AccesoCanonico()
            {
                EventoId = Guid.NewGuid().ToString(),
                Sujeto = new Sujeto { Categoria = "Empleado", Nombre = "Juan" },
                Acceso = new Acceso { Fecha = DateTime.Parse("2026-04-06T10:30:00"), Porton = "Principal" }
            };

            var enrutador = new Enrutador(_colaCanonica);
            var resultado = enrutador.ClasificarAcceso(accesoCanonico);

            Assert.AreEqual(ClasificacionAcceso.Normal, resultado);
        }

        [TestMethod]
        public void TestClasificarAcceso_EmpleadoFueraHorario()
        {
            var accesoCanonico = new AccesoCanonico()
            {
                EventoId = Guid.NewGuid().ToString(),
                Sujeto = new Sujeto { Categoria = "Empleado", Nombre = "Ana" },
                Acceso = new Acceso { Fecha = DateTime.Parse("2026-04-06T22:00:00"), Porton = "Principal" }
            };

            var enrutador = new Enrutador(_colaCanonica);
            var resultado = enrutador.ClasificarAcceso(accesoCanonico);

            Assert.AreEqual(ClasificacionAcceso.Sospechoso, resultado);
        }

        [TestMethod]
        public void TestClasificarAcceso_ProveedorDentroHorario()
        {
            var accesoCanonico = new AccesoCanonico()
            {
                EventoId = Guid.NewGuid().ToString(),
                Sujeto = new Sujeto { Categoria = "Proveedor", Nombre = "Carlos" },
                Acceso = new Acceso { Fecha = DateTime.Parse("2026-04-06T09:00:00"), Porton = "Secundario" }
            };

            var enrutador = new Enrutador(_colaCanonica);
            var resultado = enrutador.ClasificarAcceso(accesoCanonico);

            Assert.AreEqual(ClasificacionAcceso.Normal, resultado);
        }

        [TestMethod]
        public void TestClasificarAcceso_CategoriaDesconocida()
        {
            var accesoCanonico = new AccesoCanonico()
            {
                EventoId = Guid.NewGuid().ToString(),
                Sujeto = new Sujeto { Categoria = "Visitante", Nombre = "Pedro" },
                Acceso = new Acceso { Fecha = DateTime.Parse("2026-04-06T10:00:00"), Porton = "Principal" }
            };

            var enrutador = new Enrutador(_colaCanonica);
            var resultado = enrutador.ClasificarAcceso(accesoCanonico);

            Assert.AreEqual(ClasificacionAcceso.Sospechoso, resultado);
        }
    }
}
