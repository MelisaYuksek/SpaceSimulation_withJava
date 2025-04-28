/**
 * Yolcu.java
 * @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
 * @since 22.04.2025
 * <p>
 * araca ait kişiler tanımlanıyor
 * kisi sınıfından türetilmiş olup bir uzay aracında bulunan yolcuları temsil eder.
 * yolcular, gezegenler arası seyahat eder ve araca özgü konum bilgisi taşır.
 * </p>
 */

package uzaySimulasyonu;

public class Yolcu extends Kisi {
	 private String aracKonumu; // hangi aracın içindeyse konumu da odur

    public Yolcu(String isim, int yas, int kalanOmur, String uzayAraciAdi) {
        super(isim, yas, kalanOmur);
        this.aracKonumu = uzayAraciAdi;
    }

    @Override
    public String getKonum() {
        return aracKonumu;
    }

    public void setKonum(String yeniKonum) {
        this.aracKonumu = yeniKonum;
    }
}