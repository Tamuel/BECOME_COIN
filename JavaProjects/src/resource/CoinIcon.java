package resource;

import javax.swing.ImageIcon;

public enum CoinIcon {
	COIN_TAG(new ImageIcon("resources/images/coin_tag.png")),
	CON_BEACON(new ImageIcon("resources/images/coin_beacon.png")),
	COIN_TOILET(new ImageIcon("resources/images/icon_toilet.png"))
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
