/**
*
* @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
* @since 23.04.2025
* <p>	 
*  her araç bir çıkış ve varış gezegenine sahiptir.
*  araç belirli bir saatte kalkar ve belirli bir sürede varış noktasına ulaşır.
*  içinde birden fazla yolcu barındırabilir.
*  simülasyon ilerledikçe, eğer tüm yolcular ölürse araç IMHA olur.
* </p>
*/


package uzaySimulasyonu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UzayAraci {

    private String ad;
    private String cikisGezegeni;
    private String varisGezegeni;
    private int kalkisZamani; // saat cinsinden
    private int hedefeKalanSaat; // araç yolda mı, bekliyor mu?
    private LocalDate hedefeVarisTarihi = null;
    private LocalDate kalkisTarihi; // ✨ Araclar.txt'den gelen kalkış tarihi
    private boolean hareketBasladi;
    private boolean imhaOldu;

    private boolean hareketEtti = false;

    private List<Yolcu> yolcular;

    public UzayAraci(String ad, String cikis, String varis, LocalDate kalkisTarihi, int hedefeSaat) {
        this.ad = ad;
        this.cikisGezegeni = cikis;
        this.varisGezegeni = varis;
        this.kalkisZamani = 0;
        this.kalkisTarihi = kalkisTarihi;
        this.hedefeKalanSaat = hedefeSaat;
        
        this.hareketEtti = false;
        this.hareketBasladi = false;
        
        this.imhaOldu = false;
        this.yolcular = new ArrayList<>();
        this.hedefeVarisTarihi = null;
        this.kalkisZamani = 0;
        this.yolcular = new ArrayList<>();
    }

    // Tarih stringini saat cinsinden sayıya çevir
    private int tarihStringiSaateCevir(String tarihStr) {
        String[] parca = tarihStr.split("\\.");
        int gun = Integer.parseInt(parca[0]);
        int ay = Integer.parseInt(parca[1]);
        int yil = Integer.parseInt(parca[2]);

        return ((yil * 12 + (ay - 1)) * 30 + (gun - 1)) * 24;
    }

    /**
     * Araca bir yolcu eklenir
     */
    public void yolcuEkle(Yolcu y) {
        yolcular.add(y);
    }

    private long tarihFarkiGunOlarak(LocalDate baslangic, LocalDate bitis) {
        int gun1 = baslangic.getDayOfMonth();
        int ay1 = baslangic.getMonthValue();
        int yil1 = baslangic.getYear();

        int gun2 = bitis.getDayOfMonth();
        int ay2 = bitis.getMonthValue();
        int yil2 = bitis.getYear();

        int toplam1 = yil1 * 360 + (ay1 - 1) * 30 + gun1;
        int toplam2 = yil2 * 360 + (ay2 - 1) * 30 + gun2;

        return toplam2 - toplam1;
    }

    public void hedefeVarisTarihiHesapla(Map<String, Gezegen> gezegenler) {
        if (imhaOldu) return;

        Gezegen cikis = gezegenler.get(cikisGezegeni);
        Gezegen varis = gezegenler.get(varisGezegeni);

        int cikisGunSaat = cikis.getZaman().getGunUzunlugu();
        int varisGunSaat = varis.getZaman().getGunUzunlugu();

        long cikisGunFarki = tarihFarkiGunOlarak(cikis.getZaman().getTarih(), kalkisTarihi);
        int gecenSaat = (int) cikisGunFarki * cikisGunSaat;

        int toplamSaat = gecenSaat + hedefeKalanSaat;
        int varisGun = toplamSaat / varisGunSaat;

        hedefeVarisTarihi = varis.getZaman().getTarih().plusDays(varisGun);
    }

    /**
     * Bu saatlik iterasyonda neler olur?
     * - Kalkış zamanı geldiyse hareket başlar
     * - Hareket ediyorsa: kalan saat azalır
     * - Yolcular yaşlanır, ölen silinir
     * - Eğer içi boşsa araç IMHA olur
     */
    public void saatIlerle(int saatlikAdim, Map<String, Gezegen> gezegenler) {
        if (imhaOldu) return;

        for (int i = 0; i < saatlikAdim; i++) {

            // Kalkış zamanı geldi mi?
            if (!hareketEtti && kalkisGeldiMi(gezegenler)) {
                hareketEtti = true;
            }

            if (hareketEtti && hedefeKalanSaat > 0) {
                hedefeKalanSaat--;
            }

            // Yolcular yaşlanır
            Iterator<Yolcu> it = yolcular.iterator();
            while (it.hasNext()) {
                Yolcu y = it.next();
                y.saatIlerle();
                if (!y.hayattaMi()) {
                    it.remove();
                }
            }

            // 🚨 Burada artık yeni kontrol:
            if (!varisYaptiMi() && yolcular.isEmpty()) {
                // Eğer hedefe varmadıysan ve yolcular bittiyse: IMHA
                imhaOldu = true;
                hedefeKalanSaat = -1;
                hedefeVarisTarihi = null;
                break;
            }

            // 🚀 Varış
            if (hareketEtti && hedefeKalanSaat == 0 && !yolcular.isEmpty()) {
                Gezegen hedef = gezegenler.get(varisGezegeni);
                for (Yolcu y : yolcular) {
                    y.setKonum(varisGezegeni);
                    hedef.kisiEkle(y);
                }
                yolcular.clear(); // yolcular aktarıldı
                break; 
            }
        }
    }


    
 // Çıkış tarihi geldi mi?
    private boolean kalkisGeldiMi(Map<String, Gezegen> gezegenler) {
        Gezegen cikis = gezegenler.get(cikisGezegeni);
        return !cikis.getZaman().getTarih().isBefore(kalkisTarihi);
    }
    
    
    private void imhaEt() {
    	 hedefeKalanSaat = -1; // IMHA olunca -- gösterelim
    	    yolcular.clear(); // yolcuları temizle
    	    hedefeVarisTarihi = null; // hedef tarih iptal
    	    imhaOldu = true; // IMHA durumu aktif
    }
    

    public boolean imhaMi() {
        return hedefeKalanSaat == -1;
    }

    public boolean varisYaptiMi() {
        return hareketEtti && hedefeKalanSaat == 0;
    }

    public String getDurum() {
        if (imhaOldu) return "IMHA";
        if (!hareketEtti) return "Bekliyor";
        if (hareketEtti && hedefeKalanSaat > 0) return "Yolda";
        if (hareketEtti && hedefeKalanSaat == 0) return "Vardi";
        return "Bilinmiyor";
    }

    public String getAd() {
        return ad;
    }

    public String getCikis() {
        return cikisGezegeni;
    }

    public String getVaris() {
        return varisGezegeni;
    }
    
    public String getHedefeVarisTarihi() {
        if (hedefeVarisTarihi == null) return "--";
        return hedefeVarisTarihi.format(DateTimeFormatter.ofPattern("d.M.yyyy"));
    }

    public int getHedefeKalanSaat() {
        return hedefeKalanSaat >= 0 ? hedefeKalanSaat : -1;
    }
}