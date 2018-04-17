package config;

public enum BrowserEnum {
	FIREFOX("FIREFOX"),
	OPERA("OPERA"),
	INTERNETEXPLORER("INTERNETEXPLORER"),
	CHROME("CHROME"),
	EDGE("EDGE")
	;
	
	private final String browser;

    /**
     * @param text
     */
    BrowserEnum(final String browser) {
        this.browser = browser;
    }
    @Override
    public String toString() {
        return browser;
    }
}
