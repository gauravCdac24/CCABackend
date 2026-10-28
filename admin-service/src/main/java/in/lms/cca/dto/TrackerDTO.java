package in.lms.cca.dto;

import java.util.List;

public class TrackerDTO {

	private String id;
	private List<TrackerPathDTO> trackerPaths;
	private String heading;
	private String path;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<TrackerPathDTO> getTrackerPaths() {
		return trackerPaths;
	}
	public void setTrackerPaths(List<TrackerPathDTO> trackerPaths) {
		this.trackerPaths = trackerPaths;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
