package com.jphardin;

public class Input {
	KeyListener listener;
	
	public Input() {
		listener = new KeyListener();
	}
	
	public KeyListener getKeyListener() {
		return listener;
	}
}
