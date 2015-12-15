package edu.wpi.off.by.one.errors.code;

import com.sun.javafx.application.LauncherImpl;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MainPane;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main{
	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApplication.class, Preload.class, args);
	}
}