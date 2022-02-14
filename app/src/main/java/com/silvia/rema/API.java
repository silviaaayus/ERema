package com.silvia.rema;

public class API {
//    private String HOST ="http://192.168.194.78/";

    private String HOST ="https://erema.vermez.id/";
//    private String HOST ="https://erema.sislapsimpoldakaltim.com/api/";
//    private String HOST1 ="http://192.168.100.31:8001/api/";

    public String URL_FITUR = HOST+"rema/select_fitur.php";
    public String HOST_FOTO = HOST+"rema/imgprofil/";
    public String URL_GAMBAR = HOST;
    public String URL_INSERT_GMAIL = HOST+"rema/insert_gmail.php";
    public String URL_ID = HOST+"rema/select_gmail.php?email=";
    public String URL_BERITA = HOST+"rema/select_berita_all.php?kategori=";
    public String URL_BERITA_ALL = HOST+"rema/select_info_all.php";
    public String URL_LOGIN = HOST+"rema/login.php";
    public String URL_REGISTER = HOST+"rema/register.php";
    public String URL_UPI_TODAY = HOST+"rema/select_berita.php?kategori=today";
    public String URL_UPI_INFO = HOST+"rema/select_info.php";
    public String URL_EVENT_ALL = HOST+"rema/select_event_all.php";
    public String URL_EVENT_TANGGAL = HOST+"rema/select_event.php?tanggal_event=";
    public String URL_SEARCH = HOST+"rema/search_berita.php?judul=";
    public String URL_EDIT_PASSWORD = HOST+"rema/update_password.php";
    public String URL_EDIT_PROFIL = HOST+"rema/update_profil.php";
    public String URL_SELECT_USER = HOST+"rema/select_user.php";
    public String URL_SELECT_EVENT_MONTH = HOST+"rema/select_event_month.php";
    public String URL_SELECT_EVENT_MONTHYEAR = HOST+"rema/select_event_month_year.php";


}
