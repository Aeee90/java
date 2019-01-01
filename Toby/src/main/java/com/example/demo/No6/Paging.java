package tobi.No6;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Paging {
    private int rowSize;
    private int field;
    private int sort;
    private int count;
    private int pageNO;
    private int pageCount;
    private int pageStart;
    private int pageEnd;

    private String url;
    private Map<String, String> queryStr;
    private final int PAGE_BOUNDARY_SIZE = 4;

    public Paging(String pageNO, String url, String queryStr) {
    	rowSize = 10;
        this.pageNO = pageNO == null? 1 : Integer.valueOf(pageNO);
        
        this.queryStr = new HashMap<>();
        if(queryStr != null){
            try {
                queryStr = URLDecoder.decode(queryStr, "utf-8");
            } catch (Exception e) {
                queryStr ="";
            }

            this.queryStr = new HashMap<>();
            for(String str : queryStr.split("&")){
                String values[] = str.split("=");
                if(values[0].equals("pageNO") || values[0].equals(ErrorMessage.ERROR_PARAMETER) || values.length != 2) continue;
                this.queryStr.put(values[0], values[1]);
            }
        }
        this.url = url;
    }
    
    public int getPageNO() {
    	return pageNO;
    }
    
    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
    
    public int getRowStart() {
    	return rowSize * pageNO - rowSize + 1;
    }

	public int getRowEnd() {
		return rowSize * pageNO;
	}

	public int getCount() {
		return count;
	}

	public Paging setCount(int count) {
		this.count = count;
        
        return this;
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public int getPageStart() {
		return pageStart;
	}

	public int getPageEnd() {
		return pageEnd;
    }

    public String getUrl(){
        String url = this.url;

        Set<String> keys = queryStr.keySet();
        if(keys.size() > 0){
            url += "?";
            int count = 0;
            for(String key : keys){
                if(count++ > 0) url += "&";
                url += key + "=" + queryStr.get(key); 
            }
        }

        return url;
    }

    public Map<String, String> getParam(){
        return this.queryStr;
    }

    private void makePage(){
        count = count <= 0? 0 : count;
        pageCount = count/rowSize + (count%rowSize>0? 1 : 0);
		pageNO = pageNO > pageCount? pageCount : pageNO; 
		
		pageStart = pageEnd = pageNO;
		int temp = PAGE_BOUNDARY_SIZE;
		while(pageStart > 1 && temp > PAGE_BOUNDARY_SIZE/2){pageStart--; temp--;};
		while(pageEnd < pageCount && temp > 0){pageEnd++; temp--;};
        while(pageStart > 1 && temp > 0) {pageStart--; temp--;};
    }
}
