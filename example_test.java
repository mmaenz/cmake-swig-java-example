import example.Circle;
import example.Square;

public class example_test {
	public static void main(String argv[]) {
		//exampleJNI.datata();
		final Circle s = new Circle(2.0);
		System.out.println(s.area());
		System.out.println(s.perimeter());
		System.out.println(s.test().area());
		System.out.println(s.test().perimeter());
		final Circle c1 = s.test2();
		System.out.println("c1.peri: " + c1.perimeter());
		final Circle c2 = s.test2();
		System.out.println("c2.peri: " + c1.perimeter());
		final Circle c3 = c2.test2();
		System.out.println(s.test2().area());
		System.out.println(s.test2().perimeter());
		System.out.println("c1==c2: " + (c1 == c2));
		System.out.println("c2==c3: " + (c2 == c3));
		System.out.println("s==s.getParam(s): " + (s == s.getParam(s)));
		System.out.println("s==s.getThis(): " + (s == s.getThis()));
		final Triangle t = new Triangle(10);
		System.out.println("t.test2()==t.test2(): " + (t.test2() == t.test2()));
		System.out.println("t==t.getParam(t): " + (t == t.getParam(t)));
		System.out.println("t==t.getThis(): " + (t == t.getThis()));
		System.out.println("t.getToto(): " + t.getToto());

		final Square sq = new Square(1);
		System.out.println("square created");
		final Square sq1 = sq.getSubSquare();
		final Square sq2 = sq.getSubSquare();
		System.out.println("s==s: " + (sq1 == sq2));

		//s = (Circle) s.getCircle();
	}
}
