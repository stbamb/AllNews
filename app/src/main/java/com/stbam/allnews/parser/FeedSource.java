package com.stbam.allnews.parser;

/**
 * Created by Esteban on 10/24/2014.
 */
public class FeedSource {

    private String URL;
    private String URLPagina; // solo sirve para almacenar el URL de la pagina e.g. polygon.com
    private String nombre;
    private String categoria;
    private String idioma;
    private boolean aceptado = false;

    public String getURLPagina() {
        return URLPagina;
    }

    public void setURLPagina(String URLPagina) {
        this.URLPagina = URLPagina;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
