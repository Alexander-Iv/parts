package parts;

import parts.annotations.Printable;
import parts.annotations.Sortable;
import parts.entity.annotations.*;
import parts.entity.annotations.Object;

import java.io.Serializable;
import java.util.Date;

//@Entity(name = PartHtml.PARTS, nameInDB = PartDb.PARTS)
//@Entity(name = "PARTS")
@Object("Part") @TableDb("parts")
public class Part implements Serializable {
    //@Id(name = "PART_ID", viewName = "Part Id") @Sortable
    @ObjectId("Part Id") @TableDbId("PART_ID") @Sortable @Printable
    private Integer partId;

    //@Field(name = "PART_NAME", viewName = "Part Name") @Sortable
    @ObjectField("Part Name") @TableDbField("PART_NAME") @Sortable //@Printable
    private String partName;

    //@Field(name = "PART_NUMBER", viewName = "Part Number") @Sortable
    @ObjectField("Part Number") @TableDbField("PART_NUMBER") @Sortable @Printable
    private String partNumber;

    //@Field(name = "VENDOR", viewName = "Vendor") @Sortable
    @ObjectField("Vendor") @TableDbField("VENDOR") @Sortable @Printable
    private String vendor;

    //@Field(name = "QTY", viewName = "Qty") @Sortable
    @ObjectField("Qty") @TableDbField("QTY") @Sortable @Printable
    private Integer qty;

    //@Field(name = "SHIPPED", viewName = "Shipped") @Sortable
    @ObjectField("Shipped") @TableDbField("SHIPPED") @Sortable @Printable
    private Date shipped;

    //@Field(name = "RECEIVED", viewName = "Received") @Sortable
    @ObjectField("Received") @TableDbField("RECEIVED") @Sortable @Printable
    private Date received;

    public Part() {}

    public Part(Integer partId, String partName, String partNumber, String vendor, Integer qty, Date shipped, Date received) {
        this.partId = partId;
        this.partName = partName;
        this.partNumber = partNumber;
        this.vendor = vendor;
        this.qty = qty;
        this.shipped = shipped;
        this.received = received;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    //@Sortable
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    //@Sortable
    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    //@Sortable
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    //@Sortable
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    //@Sortable
    public Date getShipped() {
        return shipped;
    }

    public void setShipped(Date shipped) {
        this.shipped = shipped;
    }

    //@Sortable
    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    @Override
    public String toString() {
        return (partId==null && partName==null && partNumber==null && vendor==null && qty==null && shipped==null && received==null) ? "" : "part{" +
                "partId=" + partId +
                ", partName='" + partName + '\'' +
                ", partNumber='" + partNumber + '\'' +
                ", vendor='" + vendor + '\'' +
                ", qty=" + qty +
                ", shipped=" + shipped +
                ", received=" + received +
                "}";
    }

    public boolean isEmpty(){
        return this.equals(new Part());
    }
}