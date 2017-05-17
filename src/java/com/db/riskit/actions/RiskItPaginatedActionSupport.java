package com.db.riskit.actions;


public class RiskItPaginatedActionSupport extends RiskItActionSupport {
	private static final long serialVersionUID = 1L;
	private int currentPageCount = 1;
	private int currentPageLimit = 5;
	private boolean dataMoreThanLimit;
	private int startIndex;
	private int endIndex;
	private boolean showPrevLink = true;
	private boolean showNextLink = false;
	@Override
	public void prepare() throws Exception {
		super.prepare();
		setPaginationDetails();
	}
	public void setPaginationDetails() {
		if(currentPageCount <= 1) {
			showPrevLink = false;
			currentPageCount = 1;
		}
		else {
			showPrevLink = true;
		}
		startIndex = (currentPageCount - 1) * currentPageLimit;
		endIndex = currentPageCount * currentPageLimit;
	}
	public int getCurrentPageCount() {
		return currentPageCount;
	}
	public void setPageCount(int pageCount) {
		this.currentPageCount = pageCount;
	}	
	public int getCurrentPageLimit() {
		return currentPageLimit;
	}
	protected void setCurrentPageLimit(int currentPageLimit) {
		this.currentPageLimit = currentPageLimit;
		setPaginationDetails();
	}
	public boolean isDataMoreThanLimit() {
		return dataMoreThanLimit;
	}
	protected void setDataMoreThanLimit(boolean dataMoreThanLimit) {
		this.dataMoreThanLimit = dataMoreThanLimit;
		if(this.dataMoreThanLimit)
			this.showNextLink = true;
	}
	protected int getStartIndex() {
		return startIndex;
	}
	protected int getEndIndex() {
		return endIndex;
	}
	public boolean isShowPrevLink() {
		return showPrevLink;
	}
	public boolean isShowNextLink() {
		return showNextLink;
	}
	public int getPrevPageCount() {
		return currentPageCount - 1;
	}
	public int getNextPageCount() {
		return currentPageCount + 1;
	}
}
