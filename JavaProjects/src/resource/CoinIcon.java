package resource;

import javax.swing.ImageIcon;

public enum CoinIcon {
	TAG(new ImageIcon("resources/images/coin_tag.png")),
	BEACON(new ImageIcon("resources/images/coin_beacon.png")),
	TOILET(new ImageIcon("resources/images/icon_toilet.png"))
	;

	private ImageIcon icon;
	
	private CoinIcon() {}
	
	private CoinIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	public ImageIcon getImageIcon() {
		return this.icon;
	}
}
