package edu.wpi.off.by.one.errors.code;

import com.sun.javafx.application.LauncherImpl;

public class Main{
	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApplication.class, Preload.class, args);
	}
}