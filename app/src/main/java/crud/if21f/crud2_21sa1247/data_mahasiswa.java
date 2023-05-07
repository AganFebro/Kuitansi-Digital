package crud.if21f.crud2_21sa1247;

public class data_mahasiswa {
    private String nim;
    private String nama;
    private String fakultas;
    private String prodi;
    private String hasilgol;
    private String rjeniskelamin;
    private String tgllahir;
    private String nohp;
    private String crudemail;
    private String ipk;
    private String alamat;
    private String gambar;
    private String key;

    public String getHasilgol() {
        return hasilgol;
    }

    public void setHasilgol(String hasilgol) {
        this.hasilgol = hasilgol;
    }

    public String getRjeniskelamin() {
        return rjeniskelamin;
    }

    public void setRjeniskelamin(String rjeniskelamin) {
        this.rjeniskelamin = rjeniskelamin;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
    public data_mahasiswa() {

    }

    public data_mahasiswa(String nim, String nama, String fakultas, String prodi, String hasilgol, String rjeniskelamin, String tgllahir, String nohp, String crudemail, String ipk, String alamat, String gambar) {
        this.nim = nim;
        this.nama = nama;
        this.fakultas = fakultas;
        this.prodi = prodi;
        this.hasilgol = hasilgol;
        this.rjeniskelamin = rjeniskelamin;
        this.tgllahir = tgllahir;
        this.nohp = nohp;
        this.crudemail = crudemail;
        this.ipk = ipk;
        this.alamat = alamat;
        this.gambar = gambar;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getTgllahir() {
        return tgllahir;
    }

    public void setTgllahir(String tgllahir) {
        this.tgllahir = tgllahir;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getCrudemail() {
        return crudemail;
    }

    public void setCrudemail(String crudemail) {
        this.crudemail = crudemail;
    }

    public String getIpk() {
        return ipk;
    }

    public void setIpk(String ipk) {
        this.ipk = ipk;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
