
package com.tyfone.smartcardpersotool.model.tokenstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HwTokenLaminationInfo {

    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("dateLamination")
    @Expose
    private long dateLamination;
    @SerializedName("printingSheetPartNumber")
    @Expose
    private String printingSheetPartNumber;
    @SerializedName("overlayPartNumber")
    @Expose
    private String overlayPartNumber;
    @SerializedName("hwTokenLaminationInfocol")
    @Expose
    private Object hwTokenLaminationInfocol;
    @SerializedName("prePrintedImagePartNumber")
    @Expose
    private String prePrintedImagePartNumber;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public long getDateLamination() {
        return dateLamination;
    }

    public void setDateLamination(long dateLamination) {
        this.dateLamination = dateLamination;
    }

    public String getPrintingSheetPartNumber() {
        return printingSheetPartNumber;
    }

    public void setPrintingSheetPartNumber(String printingSheetPartNumber) {
        this.printingSheetPartNumber = printingSheetPartNumber;
    }

    public String getOverlayPartNumber() {
        return overlayPartNumber;
    }

    public void setOverlayPartNumber(String overlayPartNumber) {
        this.overlayPartNumber = overlayPartNumber;
    }

    public Object getHwTokenLaminationInfocol() {
        return hwTokenLaminationInfocol;
    }

    public void setHwTokenLaminationInfocol(Object hwTokenLaminationInfocol) {
        this.hwTokenLaminationInfocol = hwTokenLaminationInfocol;
    }

    public String getPrePrintedImagePartNumber() {
        return prePrintedImagePartNumber;
    }

    public void setPrePrintedImagePartNumber(String prePrintedImagePartNumber) {
        this.prePrintedImagePartNumber = prePrintedImagePartNumber;
    }

}
