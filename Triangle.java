import example.*;

public class Triangle extends Shape {
 double width = 2;
 Triangle t;
 public Triangle(double width) {
  //super(0L,true);
  super();
  this.width = width;
 }
 public double area() {return width*20;}
 public double perimeter() {return width*10;}
 Triangle test2() {if (t==null) t = new Triangle(5); return t;}
}

