/**
* Zaman.java
* @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
* @since 23.04.2025
* <p>	 
* Her gezegenin kendine ait zaman akışını kontrol eder.
* Saat bazında ilerler ve buna göre gün ve tarih değişir.
* 1 ay = 30 gün olarak kabul edilir.
* </p>
*/
package uzaySimulasyonu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Zaman {
    private int gunUzunlugu;         // 1 gün kaç saatten oluşuyor (ör: 20)
    private LocalDate tarih;          // Gezegenin başlangıç tarihi
    private int saatSayaci = 0;       // Toplam geçen saat
    

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy");

    /**
     * Constructor — gezegenin gün uzunluğu ve başlangıç tarihi girilir
     */
    public Zaman(int gunUzunlugu, String tarihStr) {
        this.gunUzunlugu = gunUzunlugu;
        this.tarih = LocalDate.parse(tarihStr, FORMAT);
    }

    /**
     * Zamanı 1 saat ilerletir.
     * Gerekirse gün ve ay da ilerletilir.
     */
    public void saatIlerle() {
        saatSayaci++;
        if (saatSayaci % gunUzunlugu == 0) {
            tarih = tarih.plusDays(1); // 1 gün geçti
        }
    }

    /**
     * Verilen saat kadar zamanı ilerletir.
     */
    public void saatIlerle(int saat) {
        for (int i = 0; i < saat; i++) {
            saatIlerle();
        }
    }

    /**
     * Şu anki tarih bilgisini döner.
     */
    @Override
    public String toString() {
        return tarih.format(FORMAT);
    }

    /**
     * Şu ana kadar geçen toplam saat
     */
    public int getToplamSaat() {
        return saatSayaci;
    }

    /**
     * Şu anki tarih nesnesi
     */
    public LocalDate getTarih() {
        return tarih;
    }

    public int getGunUzunlugu() {
        return gunUzunlugu;
    }
}