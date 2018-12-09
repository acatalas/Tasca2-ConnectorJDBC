package gestorestructurasgbd;

import ioc.dam.m6.exemples.comandes.Article;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//Classe que gestiona la estructura de la base de dades i les taules.
public class GestorEstructuraSGBD {

    Connection con = null;
    private String url;
    private String user;
    private String pass;

    public GestorEstructuraSGBD(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Testejam la classe "GestorEstructuraSGBD"
        /*GestorEstructuraSGBD gestor
                = new GestorEstructuraSGBD("jdbc:mysql://localhost:3306/ioc_proves", "ioc", "ioc");*/
        
        //Crea les taules a la BD
        /*gestor.crearConnexio();
        gestor.crearTaules();
        gestor.tancarConnexio();*/
        
        //Elimina les taules de la BD
        /*gestor.crearConnexio();
        gestor.eliminarTaules();
        gestor.tancarConnexio();*/
        
        //Provoca un error
        /*gestor.eliminarTaules();*/
        
        
        //Testejam la classe "GestorArticles"
        GestorArticles gestor
                = new GestorArticles("jdbc:mysql://localhost:3306/ioc_proves", "ioc", "ioc");
        
        gestor.crearConnexio();
              
        //Inserim articles a la taula
        /*gestor.inserirArticle(new Article("Microfon"));
        gestor.inserirArticle(new Article("Samsung Galaxy 10"));
        gestor.inserirArticle(new Article("Samsung Galaxy X"));
        gestor.inserirArticle(new Article("Auriculars Sony"));
        gestor.inserirArticle(new Article("Acer Aspire 5"));
        gestor.inserirArticle(new Article("Auriculares Samsung"));*/
        
        //Modificam un article de la taula
        /*gestor.modificarArticle(new Article(2, "Auriculars"));*/
        
        //Obtenim un article de la taula
        /*Article article = gestor.obtenirArticle(1);
        System.out.println("Id: " + article.getId() + "\nDescripcio: " + article.getDescripcio());*/
        
        //Obtenim tots els articles de la taula
        /*List<Article> articles = gestor.obtenirArticles();
        for (Article article : articles){
            System.out.println("ID: " + article.getId() + "\tDescripcio:\t" + article.getDescripcio());
        }*/
       
        //Obtenim tots els articles de la taula que comencen per "Samsung"
        /*List<Article> articles =  gestor.obtenirIArticlesPerDescripcio("Samsung");
        for (Article article : articles){
            System.out.println("ID: " + article.getId() + "\tDescripcio:\t" + article.getDescripcio());
        }*/
        
        //Obtenim tots els articles de la taula que comencen per "Auriculars"
        /*List<Article> articles =  gestor.obtenirIArticlesPerDescripcio("Auriculars");
        for (Article article : articles){
            System.out.println("ID: " + article.getId() + "\tDescripcio:\t" + article.getDescripcio());
        }*/
        
        //Obtenim tots els articles de la taula que comencen per "A"
        /*List<Article> articles =  gestor.obtenirIArticlesPerDescripcio("A");
        for (Article article : articles){
            System.out.println("ID: " + article.getId() + "\tDescripcio:\t" + article.getDescripcio());
        }*/
        
        //Eliminam un article de la taula
        /*gestor.eliminarArticle(2);*/
       
       gestor.tancarConnexio();
    }

    //Crea una connexió amb la base de dades definida en el constructor, amb el nom
    //i la contrassenya també passats per paràmetre.
    public void crearConnexio() {
        try {
            //Cerca la classe del controlador
            Class.forName("com.mysql.jdbc.Driver");
            
            //Crea la connexió
            con = DriverManager.getConnection(url, user, pass);
           
        } catch (ClassNotFoundException ex) {
            System.out.println("No s'ha trobat el controlador JDBC ("
                    + ex.getMessage() + ")");
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        }
        
    }

    //Tanca la connexió amb la base de dades.
    public void tancarConnexio() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        }
    }

    //Crea totes les taules de la base de dades
    public void crearTaules() {
        crearTaulaZona();
        crearTaulaSector();
        crearTaulaArticle();
        crearTaulaClient();
        crearTaulaComercial();
        
    }

    //Elimina totes les taules de la base de dades
    public void eliminarTaules() {
        Statement statement = null;
        String[] sentencies = new String[]{
            "DROP TABLE IF EXISTS article;", "DROP TABLE IF EXISTS comercial;",
            "DROP TABLE IF EXISTS client;", "DROP TABLE IF EXISTS sector;", "DROP TABLE IF EXISTS zona;"
        };
        try {
            statement = (Statement) con.createStatement();

            for (String sentenciaSQL : sentencies) {
                statement.executeUpdate(sentenciaSQL);
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancaConsulta(statement);
        }
    }

    //Crea la taula client
    private void crearTaulaClient() {
        Statement stm = null;
        String consulta;
        try {
            stm = (Statement) con.createStatement();

            consulta = "CREATE TABLE IF NOT EXISTS client("
                    + " id INTEGER NOT NULL AUTO_INCREMENT,"
                    + " nif VARCHAR(15) NOT NULL,"
                    + " nom VARCHAR(255),"
                    + " sector_id VARCHAR(10),"
                    + " zona_id VARCHAR(10),"
                    + " CONSTRAINT PK_CLIENT_ID PRIMARY KEY (id),"
                    + " CONSTRAINT FK_CLIENT_ZONA FOREIGN KEY (zona_id)"
                    + " REFERENCES zona (id)"
                    + " ON UPDATE NO ACTION ON DELETE NO ACTION,"
                    + " CONSTRAINT FK_CLIENT_SECTOR FOREIGN KEY (sector_id)"
                    + " REFERENCES sector (id)"
                    + " ON UPDATE NO ACTION ON DELETE NO ACTION,"
                    + " CONSTRAINT client_nif_key UNIQUE (nif));";
            
            stm.executeUpdate(consulta);
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            tancaConsulta(stm);
        }
    }

    private void crearTaulaComercial() {
        Statement stm = null;
        String consulta;
        try {
            stm = (Statement) con.createStatement();

            consulta = "CREATE TABLE IF NOT EXISTS comercial("
                    + " nif VARCHAR(15) NOT NULL,"
                    + " nom VARCHAR(255),"
                    + " zona_id VARCHAR(10),"
                    + " CONSTRAINT PK_COMERCIAL_NIF PRIMARY KEY (nif),"
                    + " CONSTRAINT FK_COMERCIAL_ZONA FOREIGN KEY (zona_id)"
                    + " REFERENCES zona (id)"
                    + " ON UPDATE NO ACTION ON DELETE NO ACTION);";

            stm.executeUpdate(consulta);
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            tancaConsulta(stm);
        }
    }

    //Crea la taula zona a la base de dades
    private void crearTaulaZona(){
        Statement stm = null;
        String consulta;
        try {
            stm = (Statement) con.createStatement();

            consulta = "CREATE TABLE IF NOT EXISTS zona ("
                    + " id VARCHAR(10) NOT NULL,"
                    + " descripcio VARCHAR(50),"
                    + " CONSTRAINT PK_ZONA_ID PRIMARY KEY (id));";

            stm.executeUpdate(consulta);
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            tancaConsulta(stm);
        }
    }

    //Crea la taula sector a la base de dades.
    private void crearTaulaSector() {
        Statement stm = null;
        String sentenciaSQL;
        try {
            stm = (Statement) con.createStatement();

            sentenciaSQL = "CREATE TABLE IF NOT EXISTS sector ("
                    + " id VARCHAR(10) NOT NULL,"
                    + " descripcio VARCHAR(50),"
                    + " CONSTRAINT PK_SECTOR_ID PRIMARY KEY (id));";

            stm.executeUpdate(sentenciaSQL);
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            tancaConsulta(stm);
        }
    }

    //Crea la taula article a la base de dades.
    private void crearTaulaArticle(){
        Statement stm = null;
        String consulta;
        try {
            stm = (Statement) con.createStatement();

            consulta = "CREATE TABLE IF NOT EXISTS article("
                    + " id INTEGER NOT NULL AUTO_INCREMENT,"
                    + " descripcio VARCHAR(255),"
                    + " CONSTRAINT PK_ARTICLE_ID PRIMARY KEY (id));";

            stm.executeUpdate(consulta);
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            tancaConsulta(stm);
        }
    }

    //Tanca l'objecte Statement passat per paràmetre
    public void tancaConsulta(Statement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error tancant el Statement");
        }

    }
}

