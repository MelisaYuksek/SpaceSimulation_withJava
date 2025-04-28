/**
* DosyaOkuma.java
* @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
* @since 24.04.2025
* <p>	 
* dist/ klasörü altındaki .txt dosyalarını okuyarak (Gezegenler.txt/Araclar.txt/Kisiler.txt)
* gezegen, araç ve kişi nesnelerini oluşturur.
* veriler simülasyona aktarılmak üzere işlenir
* </p>
*/
package uzaySimulasyonu;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DosyaOkuma {

    /**
     * dist/Gezegenler.txt dosyasını okuyarak gezegen nesneleri oluşturur.
     * @param dosyaYolu Dosya yolu: "dist/Gezegenler.txt"
     * @return Gezegenleri adlarıyla eşleyen Map
     */
    public static Map<String, Gezegen> gezegenleriOku(String dosyaYolu) {
        Map<String, Gezegen> gezegenler = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                String[] parca = satir.split("#");

                String ad = parca[0]; // Örn: A0
                int gunUzunlugu = Integer.parseInt(parca[1]); // Örn: 20
                String tarihStr = parca[2]; // Örn: 7.4.2025

                Zaman zaman = new Zaman(gunUzunlugu, tarihStr);
                Gezegen gezegen = new Gezegen(ad, zaman);
                gezegenler.put(ad, gezegen);
            }
        } catch (IOException e) {
            System.err.println("Gezegenler dosyası okunurken hata oluştu: " + e.getMessage());
        }

        return gezegenler;
    }

    /**
     * dist/Araclar.txt dosyasını okuyarak uzay aracı nesneleri oluşturur.
     * @param dosyaYolu Dosya yolu: "dist/Araclar.txt"
     * @return Araçları adlarıyla eşleyen Map
     */
	    public static Map<String, UzayAraci> araclariOku(String dosyaYolu) {
	        Map<String, UzayAraci> araclar = new LinkedHashMap<>();
	
	        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
	            String satir;
	            while ((satir = br.readLine()) != null) {
	                String[] parca = satir.split("#");
	
	                String ad = parca[0];
	                String cikis = parca[1];
	                String varis = parca[2];
	                DateTimeFormatter format = DateTimeFormatter.ofPattern("d.M.yyyy");
	                LocalDate kalkisTarihi = LocalDate.parse(parca[3], format);

	                int hedefeSaat = Integer.parseInt(parca[4]);
	
	
	                // UzayAraci nesnesi oluştur
	                UzayAraci arac = new UzayAraci(ad, cikis, varis, kalkisTarihi, hedefeSaat);
	
	                // Map'e ekle
	                araclar.put(ad, arac);
	            }
	        } catch (IOException e) {
	            System.err.println("Araclar dosyası okunurken hata oluştu: " + e.getMessage());
	        }
	
	        return araclar;
	    }

    /**
     * dist/Kisiler.txt dosyasını okuyarak kişi nesneleri oluşturur.
     * Kişi gezegende ise gezegene eklenir, uzay aracındaysa araca eklenir.
     * @param dosyaYolu Dosya yolu: "dist/Kisiler.txt"
     * @param gezegenler Gezegen map'i
     * @param araclar Uzay aracı map'i
     * @return kişi listesi
     */
    public static List<Kisi> kisileriOku(String dosyaYolu, Map<String, Gezegen> gezegenler, Map<String, UzayAraci> araclar) {
        List<Kisi> kisiler = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                String[] parca = satir.split("#");

                String isim = parca[0];
                int yas = Integer.parseInt(parca[1]);
                int omur = Integer.parseInt(parca[2]);
                String konum = parca[3]; // gezegen ya da araç

                if (gezegenler.containsKey(konum)) {
                    // Gezegenli sınıfı kaldırıldığı için doğrudan gezegene ekliyoruz
                    Yolcu k = new Yolcu(isim, yas, omur, konum);
                    gezegenler.get(konum).kisiEkle(k);
                    kisiler.add(k);
                } else if (araclar.containsKey(konum)) {
                    Yolcu y = new Yolcu(isim, yas, omur, konum);
                    araclar.get(konum).yolcuEkle(y);
                    kisiler.add(y);
                }
            }
        } catch (IOException e) {
            System.err.println("Kisiler dosyası okunurken hata oluştu: " + e.getMessage());
        }

        return kisiler;
    }
}