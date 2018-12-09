/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestorestructurasgbd;

import ioc.dam.m6.exemples.comandes.Article;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ale
 * Classe encaregada de gestionar la taula "Articles"
 */
public class GestorArticles {

    Connection con = null;
    private String url;
    private String user;
    private String pass;
    
    //Constructor que accepta com a parametres la direcció de la base de dades,
    //el nom de l'usuari i la contrassenya
    public GestorArticles(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }
    
    //Crea una connexió amb la base de dades
    public void crearConnexio() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            System.out.println("No s'ha trobat el controlador JDBC ("
                    + ex.getMessage() + ")");
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        }

    }

    //Tanca la connexió a la base de dades
    public void tancarConnexio() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        }
    }
    
    //Insereix un article a la base de dades
    public void inserirArticle(Article article) {
        String consulta;
        PreparedStatement stm = null;

        consulta = "INSERT INTO article VALUES(?,?)";
        try {
            stm = con.prepareStatement(consulta);

            stm.setString(1, null);
            stm.setString(2, article.getDescripcio());

            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancarStatement(stm);
        }
    }
    
    //Modifica la descripcio d'un article ja guardat a la base de dades
    public void modificarArticle(Article art) {
        String consulta;
        PreparedStatement stm = null;

        consulta = "UPDATE article SET descripcio=? WHERE id=?";
        try {
            stm = con.prepareStatement(consulta);

            stm.setString(1, art.getDescripcio());
            stm.setInt(2, art.getId());

            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancarStatement(stm);
        }
    }

    //Elimina l'article de la taula articles amb l'id passat per paràmetre
    public void eliminarArticle(int id) {
        String consulta;
        PreparedStatement stm = null;

        consulta = "DELETE FROM article WHERE ID=?";
        try {
            stm = con.prepareStatement(consulta);

            stm.setInt(1, id);

            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancarStatement(stm);
        }
    }
       
    //Obte l'article de la taula articles amb l'id passat per parametre
    public Article obtenirArticle(int id) {
        Article art = null;
        String consulta;
        PreparedStatement stm = null;
        ResultSet rs = null;

        consulta = "SELECT id, descripcio FROM article WHERE id=?";
        try {
            stm = con.prepareStatement(consulta);

            stm.setInt(1, id);

            rs = stm.executeQuery();
            if (rs.next()) {
                art = new Article(rs.getInt(1), rs.getString(2));
            }

        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancarResultSet(rs);
            tancarStatement(stm);
        }
        return art;
    }
    
    //Retorna una llista de tots els articles guardats a la taula d'articles
    public List<Article> obtenirArticles() {
        List<Article> ret = new ArrayList<>();
        String sentenciaSql;
        Statement stm = null;
        ResultSet rs = null;

        sentenciaSql = "SELECT id, descripcio FROM article";
        try {
            stm = con.createStatement();

            rs = stm.executeQuery(sentenciaSql);
            while (rs.next()) {

                ret.add(new Article(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancarResultSet(rs);
            tancarStatement(stm);
        }
        return ret;
    }

    //Retorna una llista d'articles que contenen la cadena passada
    //per parametre a la descripció
    public List<Article> obtenirIArticlesPerDescripcio(String desc){
        List<Article> ret = new ArrayList<>();
        String sentenciaSql;
        PreparedStatement stm = null;
        ResultSet rs = null;

        sentenciaSql = "SELECT id, descripcio FROM article "
                + "WHERE descripcio LIKE ?;";
        try {
            stm = con.prepareStatement(sentenciaSql);

            stm.setString(1, desc + "%");

            rs = stm.executeQuery();

            while (rs.next()) {
                ret.add(new Article(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage() + "\nEstat: " + ex.getSQLState());
        } finally {
            tancarResultSet(rs);
            tancarStatement(stm);
        }
        return ret;
    }

    //Tanca el Statement passat per parametre
    public void tancarStatement(Statement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error intentant tancar el Statement");
        }
    }
    
    //Tanca el ResultSet passat per parametre
    public void tancarResultSet(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error intentant tancar el ResultSet");
        }
    }
}
