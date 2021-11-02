package polygonmaker;

public class Settings {
	public int uuid = 0;
	public int polyId = 0;
	public int conId = 0;

	public Settings(int uuid, int polyId, int conId) {
		this.uuid = uuid;
		this.polyId = polyId;
		this.conId = conId;
	}
	
	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getPolyId() {
		return polyId;
	}

	public void setPolyId(int polyId) {
		this.polyId = polyId;
	}

	public int getConId() {
		return conId;
	}

	public void setConId(int conId) {
		this.conId = conId;
	}
}
