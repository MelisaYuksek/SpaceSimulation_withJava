/**
*
* @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
* @since 24.04.2025
* <p>
* her gezegenin kendine ait zamanı ve yaşayan kişileri vardır.
* simülasyon boyunca zaman ilerledikçe gezegenin tarihi değişir,
* gezegenliler yaşlanır ve gerekirse ölürler.
* </p>
*/


package uzaySimulasyonu;
	
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Gezegen {

    private String ad;
    private Zaman zaman;
    private int nufus;
    private List<Kisi> kisiler; // Yolcular buraya gelir

    public Gezegen(String ad, Zaman zaman) {
        this.ad = ad;
        this.zaman = zaman;
        this.nufus = 0;
        this.kisiler = new ArrayList<>();
    }

    /**
     * Bu gezegende geçen zamanı 1 saat ilerletir.
     * Her kişiyi yaşlandırır, ölenleri siler.
     */
    public void saatIlerle() {
        zaman.saatIlerle();

        Iterator<Kisi> it = kisiler.iterator();
        while (it.hasNext()) {
            Kisi k = it.next();
            k.saatIlerle();
            if (!k.hayattaMi()) {
                it.remove(); // ölen kişi silinir
                nufus--; // 🚀 Gezegenin nüfusu azalır
            }
        }
    }

    /**
     * Yeni bir kişi gezegene iniş yaptığında bu metotla eklenir.
     */
    public void kisiEkle(Yolcu yolcu) {
    	kisiler.add(yolcu);
        nufus++;
    }

    public void kisiCikar(Yolcu yolcu) {
    	if (kisiler.remove(yolcu)) {
            nufus--; // ✨ kişi çıkarıldığında nüfus azalır 
    	}
    }


    public String getAd() {
        return ad;
    }

    public Zaman getZaman() {
        return zaman;
    }
    
    public int getNufus() {
        return kisiler.size();
    }


    public List<Kisi> getKisiler() {
        return kisiler;
    }
}
