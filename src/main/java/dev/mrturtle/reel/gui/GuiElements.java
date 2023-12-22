package dev.mrturtle.reel.gui;

import dev.mrturtle.reel.item.ButtonItem;

public class GuiElements {
    public static void initialize() {
        ButtonItem.registerButton("assemble");
        ButtonItem.registerButton("disassemble");
    }
}
