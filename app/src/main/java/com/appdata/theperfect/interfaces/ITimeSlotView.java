package com.appdata.theperfect.interfaces;

import com.appdata.theperfect.model.ResponseAppointmentTimeData;
import com.appdata.theperfect.model.ResponseCategoryData;
import com.appdata.theperfect.model.ResponseSaveAppointment;
import com.appdata.theperfect.model.ResponseUpdateAppointment;

public interface ITimeSlotView extends IView {

    void onGetDataSlot(ResponseAppointmentTimeData body);

    void onSubmitData(ResponseSaveAppointment body);

    void onUpdateData(ResponseUpdateAppointment body);

}
