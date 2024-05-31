package photocatalysis;

import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.TrioSignalsIOGroup;
import com.kuka.math.geometry.Transformation;
import com.kuka.nav.Pose;
import com.kuka.nav.robot.MobileRobot;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.deviceModel.JointEnum;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.deviceModel.kmp.KmpOmniMove;
import com.kuka.task.ITaskLogger;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.MotionBatch;
import com.kuka.roboticsAPI.motionModel.OrientationReferenceSystem;
import com.kuka.roboticsAPI.motionModel.SplineOrientationType;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.JointImpedanceControlMode;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.io.*;
import java.lang.Math;


public class Table2_1_main extends RoboticsAPIApplication {
	@Inject
	private ITaskLogger _log;

	@Inject
	private LBR iiwa;
	
	Table2_actions table21 = new Table2_actions();
	
	@Named("Gripper1")
	@Inject
	Tool gripper;
	
	
	@Override
	public void initialize() {
		_log.info("*** Table2_1_main program started ***");	
		table21.initialize(gripper, iiwa, _log, "Table2_1", "1");
	}
	
	
	@Override
	public void run() throws IOException, UnknownHostException, MqttException {
		try {

			IIWA.moveToDrivePos(gripper, iiwa, _log, getApplicationControl());
			table21.detectMarkersAndInitializationFrames(gripper, iiwa, _log, "muffle", getApplicationControl());
			table21.openMuffleDoor(gripper, iiwa, _log, getApplicationControl());
			table21.moveToGeneralPoint(gripper, _log, getApplicationControl());
			table21.putTareInMuffle(gripper, _log, getApplicationControl());
			table21.moveToGeneralPoint(gripper, _log, getApplicationControl());
			table21.closeMuffleDoor(gripper, iiwa, _log, getApplicationControl());
			table21.turnOnMuffle(_log);
			table21.moveToGeneralPoint(gripper, _log, getApplicationControl());
			table21.detectMarkersAndInitializationFrames(gripper, iiwa, _log, "fume_cupboard", getApplicationControl());
			table21.closeFumeCupboard(gripper, iiwa, _log, getApplicationControl());
			IIWA.moveToDrivePos(gripper, iiwa, _log, getApplicationControl());
			
        } catch (Exception e) {
			_log.info(e.getMessage());
			getApplicationControl().pause();
        } finally {
        	MQTT.close();
        	_log.info("*** Table2_1_main program finished ***");
        }
	}
	
}



