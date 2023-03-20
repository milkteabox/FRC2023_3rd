// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;
import static frc.robot.RobotMap.*;

public class LEDSubsystem extends SubsystemBase
{
    private final AddressableLED led = new AddressableLED(0);
    private final AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(8);
    private final PowerDistribution PDH = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
    private int rainbowFirstPixelHue;
    public LEDSubsystem(){
        // PWM port 9
        // Must be a PWM header, not MXP or DIO

        // Reuse buffer
        // Default to a length of 60, start empty output
        // Length is expensive to set, so only set it once, then just update data

        led.setLength(ledBuffer.getLength());

        // Set the data
        led.setData(ledBuffer);
        led.start();
    }
    @Override
    public void periodic()
    {
        // Fill the buffer with a rainbow
        rainbow();
        // Set the LEDs
        led.setData(ledBuffer);
    }
    private void rainbow() {
        // For every pixel
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final var hue = (rainbowFirstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
            // Set the value
            ledBuffer.setHSV(i, rainbowFirstPixelHue, 255, 128);
        }
        // Increase by to make the rainbow "move"
        rainbowFirstPixelHue += 3;
        // Check bounds
        rainbowFirstPixelHue %= 180;
    }
}
