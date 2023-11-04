package frc.robot;

import java.util.Map;

public class RobotConfig {
    private FrameSize frameSize;
    private SwerveManagerCan swerveManagerCan;
    private Map<String, String> robotInfo;

    public FrameSize getFrameSize() {
        return frameSize;
    }
    public void setFrameSize(FrameSize frameSize) {
        this.frameSize = frameSize;
    }
    
    public SwerveManagerCan getSwerveManagerCan() {
        return swerveManagerCan;
    }
    public void setSwerveManagerCan(SwerveManagerCan swerveManagerCan) {
        this.swerveManagerCan = swerveManagerCan;
    }

    public Map<String, String> getRobotInfo() {
		return robotInfo;
	}
	public void setRobotInfo(Map<String, String> robotInfo) {
		this.robotInfo = robotInfo;
	}
}