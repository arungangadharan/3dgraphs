package com.j3d.graph;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Builder3D extends Application{
	
	final PhongMaterial redMaterial = new PhongMaterial();
	final PhongMaterial greenMaterial = new PhongMaterial();
	final PhongMaterial blueMaterial = new PhongMaterial();
	final PhongMaterial blackMaterial = new PhongMaterial();
	final PhongMaterial yellowMaterial = new PhongMaterial();
	final PhongMaterial orangeMaterial = new PhongMaterial();
	
	Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
	Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
	double mouseXOld = 0;
	double mouseYOld = 0;
	
	int AXIS_LENGTH = 600;
	double POS_FACTOR = 2;
	double LINE_POS_FACTOR = 1;
	
	int distanceAmongPoints = 40;
	int graphWidth = 10;
	
	

	private List<DataObject> values = new ArrayList<DataObject>();
	
	//434 328 391 462 26 89 138 245 272 5 407 314 107 212 315 318 121 42 380 54
	//85 2 335 210 435 305 58 246 243 467 94 482 66 79 353 51 49 75 23 24
	//251 377 470 466 288 122 358 439 273 290 323 359 474 14 226 497 185 390 388 324
	
    public Builder3D(){
    	
    	redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
 
        
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
 
        
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.LIGHTBLUE);
        
        blackMaterial.setDiffuseColor(Color.BLACK);
        blackMaterial.setSpecularColor(Color.GRAY);
        
        
        yellowMaterial.setDiffuseColor(Color.YELLOW);
        yellowMaterial.setSpecularColor(Color.YELLOWGREEN);
        
        orangeMaterial.setDiffuseColor(Color.ORANGE);
        orangeMaterial.setSpecularColor(Color.ORANGERED);
    	
    	
    }
    
    public void setup(){
    	
	    String xString = "434 328 391 462 26 89 138 245 272 5 407 314 107 212 315 318 121 42 380 54";
		String yString = "85 2 335 210 435 305 58 246 243 467 94 482 66 79 353 51 49 75 23 24";
		String zString = "251 377 470 466 288 122 358 439 273 290 323 359 474 14 226 497 185 390 388 324";
		
		String [] xStrArray= xString.split(" ");
		String [] yStrArray= yString.split(" ");
		String [] zStrArray= zString.split(" ");
		
		double [][] locValues = new double[20][3];
		
		for(int x=0; x<20; x++){
			
			locValues[x][0] = Double.parseDouble(xStrArray[x]);
			locValues[x][1] = Double.parseDouble(yStrArray[x]);
			locValues[x][2] = Double.parseDouble(zStrArray[x]);
		}
		
		
		setValue(locValues);
    }
    
    public void setValue(double[][] coordValues){
    	
    	for(int c = 0; c < coordValues.length; c++){
			
			DataObject dob = new DataObject(coordValues[c][0], coordValues[c][1], coordValues[c][2]);
			values.add(dob);
		}
    }
    
	@Override
	public void start(Stage stage) throws Exception {
		
		if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            throw new RuntimeException("*** ERROR: common conditional SCENE3D is not supported");
        }
		
		setup();
		
		// frame set up..
		Group root = new Group();		        
		Box xAxis = createGraph(AXIS_LENGTH, 1);
		Box yAxis = createGraph(AXIS_LENGTH, 2);
		Box zAxis = createGraph(AXIS_LENGTH, 3);
		root.getChildren().addAll(xAxis)  ;
      	root.getChildren().addAll(yAxis);
      	root.getChildren().addAll(zAxis);
		createGrpahLines (root);
		root.setTranslateX(500);
	    root.setTranslateY(200);
	    root.setRotationAxis(Rotate.X_AXIS);
	    root.setRotate(180.0);	    
        Scene scene = new Scene(root, 1500, 800,Color.BLACK);  
        Camera camera = new PerspectiveCamera();
        camera.getTransforms().addAll(xRotate, yRotate);     
        scene.setCamera(camera);

        for(DataObject dO :values){
				
				root.getChildren().add(createPoints(10.0, yellowMaterial, dO.getxVal(), dO.getyVal(), dO.getzVal()));				
		}
		for(int count = 0; count < (values.size() -1); count++){
				
				DataObject dO1 = values.get(count);
				DataObject dO2 = values.get(count+1);
				Point3D start = new Point3D(dO1.getxVal(), dO1.getyVal(), dO1.getzVal());
				Point3D end = new Point3D(dO2.getxVal(), dO2.getyVal(), dO2.getzVal());
				root.getChildren().add(createConnection(start, end));
		}
       
        EventHandler handler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	if (event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_DRAGGED){
              	  
              		double mouseXNew = event.getSceneX();
              		double mouseYNew = event.getSceneY();
              		
              		if ( event.getEventType() == MouseEvent.MOUSE_DRAGGED){
              			
              			double pitchRotate = xRotate.getAngle() + (mouseXNew - mouseXOld);        			
//              			pitchRotate = pitchRotate > cameraYLimit ? cameraYLimit :         			
//              			xRotate.setAngle(pitchRotate);
              			double yawRotate = yRotate.getAngle() + (mouseYNew - mouseYOld);
              			yRotate.setAngle(yawRotate);        			
              		}
              		
              		mouseXOld = mouseXNew;
              		mouseYOld = mouseYNew;
              	}
            }
          };
          
        scene.addEventHandler(MouseEvent.ANY, handler);
	    stage.setTitle("Drawing a Sphere"); 
	    stage.setScene(scene); 
	    stage.show(); 
	}
	
	private Box createGraph(int axisLength, int axisOption){
		
		Box xAxis = null;
		if(axisOption == 1){
			xAxis = createLines(AXIS_LENGTH, POS_FACTOR, POS_FACTOR, redMaterial);
			xAxis.setTranslateX(AXIS_LENGTH/2);
		}else if(axisOption == 2){
			xAxis = createLines(POS_FACTOR, AXIS_LENGTH, POS_FACTOR, greenMaterial);	
			xAxis.setTranslateY(AXIS_LENGTH/2);
		}else if(axisOption == 3){
			xAxis = createLines(POS_FACTOR, POS_FACTOR, AXIS_LENGTH, blueMaterial);
			xAxis.setTranslateZ(AXIS_LENGTH/2);
		}		
		
		return xAxis;
	}
	
	private Sphere createPoints( double sphereRadius, PhongMaterial spehereMaterial, double xPosition, double yPosition, double zPosition){
		
		 Sphere sphere = new Sphere();
		 sphere.setRadius(sphereRadius);   
		 sphere.setMaterial(spehereMaterial);		 
		 sphere.setTranslateX(xPosition); 
		 sphere.setTranslateY(yPosition);
		 sphere.setTranslateZ(zPosition);		 
		 return sphere;
	}
	
	private Box createLines(double xStart, double yStart, double zStart, PhongMaterial m){
		
		 final Box box = new Box(xStart, yStart, zStart);
		 box.setMaterial(m);		
		 return box;
	}
	
		
	public Cylinder createConnection(Point3D origin, Point3D target) {
		
	    Point3D yAxis = new Point3D(0, 1, 0);
	    Point3D diff = target.subtract(origin);
	    double height = diff.magnitude();
	    Point3D mid = target.midpoint(origin);
	    Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());
	    Point3D axisOfRotation = diff.crossProduct(yAxis);
	    double angle = Math.acos(diff.normalize().dotProduct(yAxis));
	    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
	    Cylinder line = new Cylinder(1, height);
	    line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
	    return line;
	}
	
	private void createGrpahLines(Group root){
		
		for(int count =0; count <=AXIS_LENGTH; count = count+graphWidth  ){
			
			Box xAxisAtYEnd =  createLines(AXIS_LENGTH, POS_FACTOR, POS_FACTOR, redMaterial);
			xAxisAtYEnd.setTranslateX(AXIS_LENGTH/2); //X-axis towards Y
			xAxisAtYEnd.setTranslateY(count);
			root.getChildren().addAll(xAxisAtYEnd);
		}
		
		for(int count =0; count <=AXIS_LENGTH; count= count+graphWidth  ){
			
			Box xAxisAtZEnd =  createLines(AXIS_LENGTH, POS_FACTOR, POS_FACTOR, greenMaterial);
			xAxisAtZEnd.setTranslateX(AXIS_LENGTH/2); //X-axis towards Z
			xAxisAtZEnd.setTranslateZ(count);
			root.getChildren().addAll(xAxisAtZEnd);
		}
		
		
		
		for(int count =0; count <=AXIS_LENGTH; count= count+graphWidth  ){
			Box yAxisAtXEnd = createLines(POS_FACTOR, AXIS_LENGTH, POS_FACTOR, redMaterial);	
			yAxisAtXEnd.setTranslateY(AXIS_LENGTH/2);//Y-axis towards X
			yAxisAtXEnd.setTranslateX(count);
			root.getChildren().addAll(yAxisAtXEnd);
		}
		
		for(int count =0; count <=AXIS_LENGTH; count= count+graphWidth  ){
			
			Box yAxisAtZEnd = createLines(POS_FACTOR, AXIS_LENGTH, POS_FACTOR, blueMaterial);	
			yAxisAtZEnd.setTranslateY(AXIS_LENGTH/2);//Y-axis towards Z
			yAxisAtZEnd.setTranslateZ(count);
			root.getChildren().addAll(yAxisAtZEnd);
		}
		
		for(int count =0; count <=AXIS_LENGTH; count= count+graphWidth  ){
			
			Box zAxisAtXEnd = createLines(POS_FACTOR, POS_FACTOR, AXIS_LENGTH, greenMaterial);
			zAxisAtXEnd.setTranslateZ(AXIS_LENGTH/2);//Z-axis towards X
			zAxisAtXEnd.setTranslateX(count);
			root.getChildren().addAll(zAxisAtXEnd);
		}
		for(int count =0; count <=AXIS_LENGTH; count= count+graphWidth  ){
			
			Box zAxisAtYEnd = createLines(POS_FACTOR, POS_FACTOR, AXIS_LENGTH, blueMaterial);
			zAxisAtYEnd.setTranslateZ(AXIS_LENGTH/2);
			zAxisAtYEnd.setTranslateY(count); //Z axis towards Y
			root.getChildren().addAll(zAxisAtYEnd);
		}
	}
	
	
	

	public static void main(String[] args) {
		
		
		
		launch(args);
	}


	class DataObject{
		
		DataObject(double xVal, double yVal, double zVal){
			
			this.xVal = xVal;
			this.yVal = yVal;
			this.zVal = zVal;
		}
		private double xVal;
		public double getxVal() {
			return xVal;
		}
		public void setxVal(double xVal) {
			this.xVal = xVal;
		}
		public double getyVal() {
			return yVal;
		}
		public void setyVal(double yVal) {
			this.yVal = yVal;
		}
		public double getzVal() {
			return zVal;
		}
		public void setzVal(double zVal) {
			this.zVal = zVal;
		}
		private double yVal;
		private double zVal;
	}
	

}
