package GenericAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GenericAdapter<T> extends ArrayAdapter<T>{

	private final LayoutInflater _inflater;
	private final HashMap<String, Method> _getters;
	private final int _resource;
	private final String _packageName;
	
	private final IViewBinder _viewBinder;
	
	public GenericAdapter(Context contexto,int resource,
			List<T> elements, String packageName, IViewBinder viewBinder)
	{
		super(contexto,0,elements);
	
		_inflater=LayoutInflater.from(contexto);
		_getters= new HashMap<String,Method>();
		_resource=resource;
		_packageName=packageName;
		_viewBinder=viewBinder;
		
		if(super.getCount()>0)	
			LoadGetters();
	}
		
	private HashMap<String, View> viewsDictionary;
	private View actualView;
	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(_getters.size()==0)
			LoadGetters();		
		
		if(convertView==null)
		{
			viewsDictionary=new HashMap<String, View>();
			actualView=_inflater.inflate(_resource, null);
			
			for(String nombreGetter: _getters.keySet())
			{
				int id=actualView.getResources().getIdentifier(
						nombreGetter,"id",_packageName);
				viewsDictionary.put(nombreGetter, actualView.findViewById(id));
			}
			
			actualView.setTag(viewsDictionary);
		}
		else
		{
			actualView=convertView;
			viewsDictionary=(HashMap<String, View>)actualView.getTag();
		}
		
		for(String getterName: _getters.keySet())
		{
			Object resultado;
			Method metodo=_getters.get(getterName);
			
			try {
				resultado=metodo.invoke(super.getItem(position));				
			} catch (Exception e){
				return null;
			}
			
			String tipoGetter=metodo.getReturnType().getSimpleName();
			View vistaGetter=viewsDictionary.get(getterName);
			
			_viewBinder.BindView(metodo.getName(), tipoGetter, vistaGetter, resultado);			
		}
		
		return actualView;
	}
	
	private void LoadGetters()
	{
		Method[] methods = super.getItem(0).getClass().getMethods();
		for(Method method:methods)
		{
			String name=method.getName().toUpperCase();
			if(name.startsWith("GET") && !name.contains("CLASS")) 
				_getters.put(method.getName().substring(3), method);			
		}
	}
}
