package org.frojoe.l5rSearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.frojoe.l5rSearch.util.Constants;
import org.frojoe.l5rSearch.widget.RangeSeekBar;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class Query {

	List<BasicNameValuePair> params;
			
	public Query() {
		params = new ArrayList<BasicNameValuePair>();
	}
	
	public void processEditTexts(EditText... editTexts) {
		for (EditText et : editTexts) {
			String name = getSingleParamName(et);
			String value = et.getText().toString();
			params.add(new BasicNameValuePair(name, value));
		}
	}
	
	public void processSpinners(Spinner... spinners) {
		for (Spinner sp : spinners) {
			String name = getSingleParamName(sp);
			String value = sp.getSelectedItem().toString();
			if (!value.equals(getSpinnerDefault(sp))) {
				String trueValue = Constants.SPINNER_VALUE_MAP.get(value);
				params.add(new BasicNameValuePair(name, trueValue));
			} else
				params.add(new BasicNameValuePair(name, ""));
		}
	}

    public void processCheckboxes(CheckBox... checkBoxes) {
        for (CheckBox cb : checkBoxes) {
            String name = getSingleParamName(cb);
            if (cb.isChecked())
                params.add(new BasicNameValuePair(name, "1"));
        }
    }
	
	//each seek bar is grouped with their checkbox
	public void processIntegerSeekBarGroups(
			View[]... integerSeekBarGroups) {
		
		for (View[] group : integerSeekBarGroups) {
			RangeSeekBar<Integer> isb = (RangeSeekBar<Integer>) group[0];
			CheckBox cb = (CheckBox) group[1];
			if (cb.isChecked()) {
				String[] names = getMultiParamNames(isb);
				Integer low = isb.getSelectedMinValue();
				Integer high = isb.getSelectedMaxValue();
				params.add(new BasicNameValuePair(names[0], low.toString()));
				params.add(new BasicNameValuePair(names[1], high.toString()));
			}
		}
	}
	
	private String getSingleParamName(View view) {
		int id = view.getId();
		return Constants.VIEW_PARAM_MAP.get(id);
	}
	
	private String[] getMultiParamNames(RangeSeekBar rsb) {
		int id = rsb.getId();
		return Constants.VIEW_MULTI_PARAM_MAP.get(id);
	}
	
	private String getSpinnerDefault(Spinner sp) {
		int id = sp.getId();
		return Constants.SPINNER_DEFAULT_MAP.get(id);
	}
	
	public BasicNameValuePair[] getParamsAsArray() {
		BasicNameValuePair[] paramArray = new BasicNameValuePair[params.size()];
		for (int i = 0; i < params.size(); i++)
			paramArray[i] = params.get(i);
		return paramArray;
	}
	
}
