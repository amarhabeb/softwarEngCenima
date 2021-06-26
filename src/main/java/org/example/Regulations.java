package org.example;

public class Regulations {
    private boolean regulations;	// will be false if there is no limitations
    private int Y;	// this is the same Y described in the requirements file


    public Regulations (boolean regulations, int Y){
        this.regulations = regulations;
        this.Y = Y;
    }

    public boolean getRegulations() {
        return regulations;
    }

    public void setRegulations(boolean regulations) {
        this.regulations = regulations;
    }

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
}
