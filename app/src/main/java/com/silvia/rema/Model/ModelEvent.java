package com.silvia.rema.Model;



public class ModelEvent {

        private String id;
        private String nama_event;
        private String tanggal_event;
        private String lokasi_event;
        private String jam_mulai;
        private String keterangan;
        private String status;
        private String created_at;
        private String updated_at;

    public ModelEvent(String id, String nama_event, String tanggal_event, String lokasi_event, String jam_mulai, String keterangan, String status, String created_at, String updated_at) {
        this.id = id;
        this.nama_event = nama_event;
        this.tanggal_event = tanggal_event;
        this.lokasi_event = lokasi_event;
        this.jam_mulai = jam_mulai;
        this.keterangan = keterangan;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_event() {
        return nama_event;
    }

    public void setNama_event(String nama_event) {
        this.nama_event = nama_event;
    }

    public String getTanggal_event() {
        return tanggal_event;
    }

    public void setTanggal_event(String tanggal_event) {
        this.tanggal_event = tanggal_event;
    }

    public String getLokasi_event() {
        return lokasi_event;
    }

    public void setLokasi_event(String lokasi_event) {
        this.lokasi_event = lokasi_event;
    }

    public String getJam_mulai() {
        return jam_mulai;
    }

    public void setJam_mulai(String jam_mulai) {
        this.jam_mulai = jam_mulai;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
