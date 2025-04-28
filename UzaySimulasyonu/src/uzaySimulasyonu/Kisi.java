/**
* Kisi.java
* @author Melisa Yüksek / melisa.yuksek@ogr.sakarya.edu.tr
* @since 22.04.2025
* <p>
* 	[ortak temel yapı]
	tüm kişilerin (yolcu) ortak özelliklerini (isim,ömür) tanımlar. ama konumunu bilemez. 
 *  soyut bir sınıftır, doğrudan nesnesi oluşturulmaz.
 *  kalıtım yoluyla Yolcu veya Gezegenli gibi alt sınıflar tarafından genişletilir.
 * </p>
*/

package uzaySimulasyonu;

public abstract class Kisi {
	protected String isim;
	protected int yas;
	protected int kalanOmur;

	public Kisi(String isim,int yas, int kalanOmur) {
		this.isim=isim;
		this.yas=yas;
		this.kalanOmur=kalanOmur;
	}

	//her saat başı yaş artar, kalan ömür azalır
	public void saatIlerle() {
		if (kalanOmur>0) {
			kalanOmur--;
		}
	}

	public boolean hayattaMi() {
		return kalanOmur>0;
	}

	// getterlar
	public String getIsim() {
		return isim;
	}
	
	  public int getYas() {
	        return yas;
	    }

	public int getKalanOmur() {
		return kalanOmur;
	}
	
	// alt sınıflar bu metodu override etmek zorunda
		public abstract String getKonum();


	// nesnenin metinsel temsilini verir
	@Override
	public String toString() {
		return isim + " (kalan ömür: " + kalanOmur + " saat)";
	}
}
