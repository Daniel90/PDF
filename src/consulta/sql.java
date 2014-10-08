/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consulta;
import java.sql.*;

/**
 *
 * @author AMaroevich
 */
public class sql {
    public ResultSet documentosPower(int fechaInicial, int fechaFinal,int mes,int ano){
        ResultSet resultadoSql = null;
        
        Connection punteroConexion = null;
        String database_clmhp403 = "mhexidat.sfp187n";  //base de datos

        String ipserver = "192.168.2.118";
        String user = "USRCLIENTE";
        String pass = "CLIENTE";
        
        try{
            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
            String urlconexion_clmhp403 = "jdbc:as400://" + ipserver + "/" + database_clmhp403;

            try{
                punteroConexion = DriverManager.getConnection(urlconexion_clmhp403, user, pass);
                switch(mes){
                    case 01:
                        String sql_clmhp403 = "select famn87,desn87,v01n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp403);
                        Statement s_clmhp403 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp403.executeQuery(sql_clmhp403);
                        break;
                    case 02:
                        String sql_clmhp404 = "select famn87,desn87,v01n87,v02n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp404);
                        Statement s_clmhp404 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp404.executeQuery(sql_clmhp404);
                        break;
                     case 03:
                        String sql_clmhp405 = "select famn87,desn87,v01n87,v02n87,v03n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp405);
                        Statement s_clmhp405 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp405.executeQuery(sql_clmhp405);
                        break;
                     case 04:
                        String sql_clmhp406 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp406);
                        Statement s_clmhp406 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp406.executeQuery(sql_clmhp406);
                        break;
                     case 05:
                        String sql_clmhp407 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp407);
                        Statement s_clmhp407 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp407.executeQuery(sql_clmhp407);
                        break;
                     case 06:
                        String sql_clmhp408 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp408);
                        Statement s_clmhp408 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp408.executeQuery(sql_clmhp408);
                        break;
                     case 07:
                        String sql_clmhp409 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87,v07n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp409);
                        Statement s_clmhp409 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp409.executeQuery(sql_clmhp409);
                        break;
                     case 8:
                        String sql_clmhp410 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87,v07n87,v08n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp410);
                        Statement s_clmhp410 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp410.executeQuery(sql_clmhp410);
                        break;
                     case 9:
                        String sql_clmhp411 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87,v07n87,v08n87,v09n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp411);
                        Statement s_clmhp411 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp411.executeQuery(sql_clmhp411);
                        break;
                     case 10:
                        String sql_clmhp412 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87,v07n87,v08n87,v09n87,v10n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp412);
                        Statement s_clmhp412 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp412.executeQuery(sql_clmhp412);
                        break;
                     case 11:
                        String sql_clmhp413 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87,v07n87,v08n87,v09n87,v10n87,v11n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp413);
                        Statement s_clmhp413 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp413.executeQuery(sql_clmhp413);
                        break;
                     case 12:
                        String sql_clmhp414 = "select famn87,desn87,v01n87,v02n87,v03n87,v04n87,v05n87,v06n87,v07n87,v08n87,v09n87,v10n87,v11n87,v12n87 from " + database_clmhp403;
                        System.out.println("sql a power7: " + sql_clmhp414);
                        Statement s_clmhp414 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp414.executeQuery(sql_clmhp414);
                        break;
                }
                //String sql_clmhp403 = "select famn87,desn87,v01n87 from " + database_clmhp403;
                //select * from mhexidat.docf0108 where ffec01 >= 20121001 and ffec01 <= 20121031
                //System.out.println("sql a power7: " + sql_clmhp403);
                //Statement s_clmhp403 = punteroConexion.createStatement();
                //resultadoSql = s_clmhp403.executeQuery(sql_clmhp403);
                }catch(Exception e){
                e.printStackTrace();
            }        
                    
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultadoSql;
    }
}
