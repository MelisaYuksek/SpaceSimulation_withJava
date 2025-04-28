/**
* Simulasyon.java
* @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
* @since 22.04.2025
* <p>
* 	Simülasyon sınıfı — tüm işlemleri kontrol eder
 * Zaman ilerletme, dosya okuma ve simülasyon çıktısını yönetir
* </p>
*/
package uzaySimulasyonu;
import java.util.*;

public class Simulasyon {

    private Map<String, Gezegen> gezegenler;
    private Map<String, UzayAraci> araclar;
    private List<Kisi> kisiler; //kişiler listesi yalnızca dosyadan okuma sırasında geçici olarak kullanılır

    private int globalSaat = 0;

    public Simulasyon() {
        gezegenler = DosyaOkuma.gezegenleriOku("Gezegenler.txt");
        araclar = DosyaOkuma.araclariOku("Araclar.txt");
        kisiler = DosyaOkuma.kisileriOku("Kisiler.txt", gezegenler, araclar);
        
        for (UzayAraci a : araclar.values()) {
            a.hedefeVarisTarihiHesapla(gezegenler);
        }
    }
    
    
    public static void temizleKonsol() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Konsol temizlenemedi.");
        }
    }


    /**
     * Belirli sayıda saat simülasyonu ilerletir
     */
    private void simSaatIlerle(int saat) {
        for (int i = 0; i < saat; i++) {
            globalSaat++;

            for (Gezegen g : gezegenler.values()) {
                g.saatIlerle();
            }

            for (UzayAraci a : araclar.values()) {
                a.saatIlerle(1, gezegenler);
            }
        }
        System.out.println("\n Toplam geçen saat: " + globalSaat + " saat.\n");
    }

  

    /**
     * Java'nın ana başlangıç noktası — Simülasyon burada başlar.
     */
    public static void main(String[] args) {
    	
        Simulasyon sim = new Simulasyon();
        sim.baslat();
    }

    /**
     * Simülasyonu başlatan ana döngü
     */
    
    private boolean tumAraclarTamamlandi() {
        for (UzayAraci arac : araclar.values()) {
            if (!arac.imhaMi() && !arac.varisYaptiMi()) {
                return false; // hâlâ yolda olan araç var
            }
        }
        return true; // hepsi vardı veya imha oldu
    }

    public void baslat() {
        System.out.println(" Simülasyon Başlıyor ");

        while (!tumAraclarTamamlandi()) {
            temizleKonsol();
            durumYazdir();

            simSaatIlerle(1);  // 1 saat ilerle

            try {
                Thread.sleep(50); // küçük bir bekleme efekti, ekranı okumak için (50ms)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Simülasyon bittiğinde
        temizleKonsol();
        durumYazdir();
        System.out.println("\nTüm araçlar varış noktasına ulaştı.");
        
    }



  

    /**
     * Simülasyon tamamlandığında tüm durumları yazdırır
     */
    private void durumYazdir() {
        System.out.println("\n=== GÜNCEL DURUM ===");

        System.out.println("Toplam Geçen Saat: " + globalSaat + " saat\n");
        
        // Gezegenleri yazdır
        System.out.println("\nGezegenler:\n");

        System.out.printf("%-12s", ""); 
        for (Gezegen g : gezegenler.values()) {
            System.out.printf("%-12s", g.getAd());
        }
        System.out.println();

        System.out.printf("%-12s", "Tarih:");
        for (Gezegen g : gezegenler.values()) {
            System.out.printf("%-12s", g.getZaman().toString());
        }
        System.out.println();

        System.out.printf("%-12s", "Nüfus:");
        for (Gezegen g : gezegenler.values()) {
            System.out.printf("%-12d", g.getNufus());
        }
        System.out.println();

        // Uzay araçlarını yazdır
        System.out.println("\nUzay Araçları:\n");
        System.out.printf("%-20s %-15s %-15s %-15s %-20s %-25s\n",
            "Araç Adı", "Durum", "Çıkış", "Varış", "Hedefe Kalan Saat", "Hedefe Varacağı Tarih");

        for (UzayAraci a : araclar.values()) {
            String kalanSaatStr = a.getHedefeKalanSaat() >= 0 ? String.valueOf(a.getHedefeKalanSaat()) : "--";
            String varisTarihi = (!a.imhaMi() && a.getHedefeVarisTarihi() != null) ? a.getHedefeVarisTarihi() : "--";

            System.out.printf("%-20s %-15s %-15s %-15s %-20s %-25s\n",
                a.getAd(), a.getDurum(), a.getCikis(), a.getVaris(), kalanSaatStr, varisTarihi);
            
            
        }
    }
}