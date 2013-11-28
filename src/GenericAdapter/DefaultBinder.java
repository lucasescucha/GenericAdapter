package GenericAdapter;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

public class DefaultBinder implements IViewBinder{

	@Override
	public void BindView(String nombrePropiedad, String tipoPropiedad,
			Object vista, Object valorPropiedad) {
		if(tipoPropiedad.equals("String"))
			((TextView)vista).setText((String)valorPropiedad);
		else if(tipoPropiedad.equals("Bitmap"))				
			((ImageView)vista).setImageBitmap((Bitmap)valorPropiedad);		
		else if(tipoPropiedad.equals("Integer"))				
			((TextView)vista).setText(((Integer)valorPropiedad).toString());
	}
}
