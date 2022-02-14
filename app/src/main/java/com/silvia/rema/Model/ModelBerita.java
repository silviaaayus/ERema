package com.silvia.rema.Model;

import java.io.Serializable;

public class ModelBerita{




        private String judul;
        private String isi;
        private int user_id_penulis;
        private String waktu_rilis;
        private String kategori;
        private String thumbnail;
        private String name;

    public ModelBerita(String judul, String isi, int user_id_penulis, String waktu_rilis, String kategori, String thumbnail, String name) {
        this.judul = judul;
        this.isi = isi;
        this.user_id_penulis = user_id_penulis;
        this.waktu_rilis = waktu_rilis;
        this.kategori = kategori;
        this.thumbnail = thumbnail;
        this.name = name;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public int getUser_id_penulis() {
        return user_id_penulis;
    }

    public void setUser_id_penulis(int user_id_penulis) {
        this.user_id_penulis = user_id_penulis;
    }

    public String getWaktu_rilis() {
        return waktu_rilis;
    }

    public void setWaktu_rilis(String waktu_rilis) {
        this.waktu_rilis = waktu_rilis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
