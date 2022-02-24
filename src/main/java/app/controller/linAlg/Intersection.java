package app.controller.linAlg;

public abstract class Intersection
{
    public static boolean findIntersection(Vector p_1, Vector p_2, Vector p_3, Vector p_4)
    {
        double x_1 = p_1.getX();
        double y_1 = p_1.getY();
        double x_2 = p_2.getX();
        double y_2 = p_2.getY();
        double x_3 = p_3.getX();
        double y_3 = p_3.getY();
        double x_4 = p_4.getX();
        double y_4 = p_4.getY();

        double dom = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);
        double t = ((x_1 - x_3)*(y_3 - y_4) - (y_1 - y_3)*(x_3 - x_4))/ dom;
        double u = ((x_1 - x_3)*(y_1 - y_2) - (y_1 - y_3)*(x_1 - x_2))/dom;

        return (t <= 1 && t >= 0  && u <= 1 && u >= 0);
    }
}
