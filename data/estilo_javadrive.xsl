<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/">
    <html>
      <head>
        <meta charset="UTF-8"/>
        <title>Sistema de Gestion JavaDrive</title>
        <script src="https://cdn.tailwindcss.com"></script>
      </head>
      <body class="font-sans m-5 bg-gray-100">

        <h1 class="bg-blue-900 text-white p-4 m-0 text-2xl font-bold">
          JavaDrive
        </h1>

        <div class="bg-blue-100 p-3 mb-5 border-l-4 border-blue-900 space-x-4">
          <strong>Empresa: </strong> <xsl:value-of select="javadrive/empresa/nombre"/>
          <strong>Ubicacion: </strong> <xsl:value-of select="javadrive/empresa/ubicacion"/>
        </div>

        <h2 class="text-blue-900 border-b-2 border-blue-900 pb-1 text-xl font-semibold">
          Inventario de Flota
        </h2>

        <table class="w-full border-collapse mb-8 bg-white">
          <tr>
            <th class="bg-blue-900 text-white p-2 text-left">Tipo</th>
            <th class="bg-blue-900 text-white p-2 text-left">Matricula</th>
            <th class="bg-blue-900 text-white p-2 text-left">Marca y Modelo</th>
            <th class="bg-blue-900 text-white p-2 text-left">Especificaciones</th>
            <th class="bg-blue-900 text-white p-2 text-left">Disponible</th>
          </tr>

          <xsl:for-each select="javadrive/flota/vehiculo">
            <tr class="hover:bg-blue-50">
              <td>
                <xsl:choose>
                  <xsl:when test="@tipo='Coche'">
                    <span class="bg-blue-200 text-blue-800 px-2 py-1 rounded font-bold">
                      Coche
                    </span>
                  </xsl:when>
                  <xsl:otherwise>
                    <span class="bg-green-200 text-green-800 px-2 py-1 rounded font-bold">
                      Furgoneta
                    </span>
                  </xsl:otherwise>
                </xsl:choose>
              </td>

              <td class="p-2 border-b border-gray-300 font-bold">
                <xsl:value-of select="matricula"/>
              </td>

              <td class="p-2 border-b border-gray-300">
                <xsl:value-of select="marca"/> - <xsl:value-of select="modelo"/>
              </td>

              <td class="p-2 border-b border-gray-300">
                <xsl:value-of select="especifico"/>
              </td>

              <td class="p-2 border-b border-gray-300">
                <xsl:choose>
                  <xsl:when test="disponible='true'">
                    <span class="text-green-600 font-bold">Si</span>
                  </xsl:when>
                  <xsl:otherwise>
                    <span class="text-red-600 font-bold">No</span>
                  </xsl:otherwise>
                </xsl:choose>
              </td>
            </tr>
          </xsl:for-each>
        </table>

        <h2 class="text-blue-900 border-b-2 border-blue-900 pb-1 text-xl font-semibold">
          Listado de Clientes
        </h2>

        <table class="w-full border-collapse mb-8 bg-white">
          <tr>
            <th class="bg-blue-900 text-white p-2 text-left">DNI</th>
            <th class="bg-blue-900 text-white p-2 text-left">Nombre Completo</th>
            <th class="bg-blue-900 text-white p-2 text-left">Telefono</th>
          </tr>

          <xsl:for-each select="javadrive/clientes/cliente">
            <tr class="hover:bg-blue-50">
              <td class="p-2 border-b border-gray-300">
                <xsl:value-of select="dni"/>
              </td>
              <td class="p-2 border-b border-gray-300">
                <xsl:value-of select="nombre"/>
              </td>
              <td class="p-2 border-b border-gray-300">
                <xsl:value-of select="telefono"/>
              </td>
            </tr>
          </xsl:for-each>
        </table>

      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>