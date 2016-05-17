package main;

/**
 *
 * Old spherical method
 *
 * */


//public class SphericalCalculation extends Calculation {
//
//	private static final double MIN_THETA = 0.1 * Math.PI;
//
//	// Anisotropy axe's angles
//	private final double at;
//	private final double af;
//
//	// m_theta, m_fi
//	public double theta;
//	public double fi;
//
//	//Circular field's characteristics
//	private final double h;
//	private final double w;
//
//	public SphericalCalculation(double theta, double fi, double h, double w) {
//		this.at = theta;
//		this.af = fi;
//
//		if (theta < MIN_THETA)
//			this.theta = MIN_THETA;
//		else
//			this.theta = theta;
//		this.fi = fi;
//
//		this.h = h;
//		this.w = w;
//	}
//
//	protected void iteration() {
//		double[] k1 = LLG(theta, fi, t);
//		double[] k2 = LLG(theta + k1[0] * dt/2, fi + k1[1] * dt/2, t + dt/2);
//		double[] k3 = LLG(theta + k2[0] * dt/2, fi + k2[1] * dt/2, t + dt/2);
//		double[] k4 = LLG(theta + k3[0] * dt, fi + k3[1] * dt, t + dt);
//
//		theta += dt/6 * (k1[0] + 2 * k2[0] + 2 * k3[0] + k4[0]);
//		fi += dt/6 * (k1[1] + 2 * k2[1] + 2 * k3[1] + k4[1]);
//		t += dt;
//	}
//
//	private double hx(double mt, double mf, double t) {
//		return h * Math.cos(w * t);
//	}
//
//	private double hy(double mt, double mf, double t) {
//		return h * Math.sin(w * t);
//	}
//
//	private double hz(double mt, double mf, double t) {
//		return 0;
//	}
//
//	private double m_p(double mt, double mf) {
//		return sin(mt) * sin(at) * cos(mf - af) + cos(mt) * cos(at);
//	}
//
//	private double[] LLG(double mt, double mf, double t) {
//
//		double hx = hx(mt, mf, t);
//		double hy = hy(mt, mf, t);
//		double hz = hz(mt, mf, t);
//
//		//m_parallel
//		double mp = m_p(mt, mf);
//
//		//m_theta
//		double theta =
//			(hy * cos(mf) - hx * sin(mf)) + mp * sin(at) * sin(af - mf) +
//			ALPHA * cos(mt) * ((hx * cos(mf) + hy * sin(mf)) + mp * sin(at) * cos(af - mf)) -
//			ALPHA * sin(mt) * (hz + mp * cos(at));
//
//		//m_fi
//		double phi =
//				- cos(mt) / sin(mt) *
//					(hx * cos(mf) + hy * sin(mf) +
//					mp * sin(at) * cos(af - mf)) + hz + mp * cos(at) +
//				ALPHA / sin(mt) * (hy * cos(mf) - hx * sin(mf)) +
//				ALPHA / sin(mt) * mp * sin(at) * sin(af - mf);
//		phi /= ALPHA * ALPHA + 1;
//		theta /= ALPHA * ALPHA + 1;
//		return new double[]{theta, phi};
//	}
//
//
//	public void run(double waitingTime, double workingTime) {
//		wait(waitingTime);
//
//		while (true) {
//			iteration();
//			array.add(new Vector(theta, fi));
//			if (t >  waitingTime + workingTime)
//				break;
//		}
//	}
//
//	@Deprecated
//	public void run() {	}
//
//}
