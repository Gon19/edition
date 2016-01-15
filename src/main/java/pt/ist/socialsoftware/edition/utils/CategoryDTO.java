package pt.ist.socialsoftware.edition.utils;

import java.io.Serializable;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String text;
	private String selected;

	public CategoryDTO() {
	}

	public CategoryDTO(VirtualEdition virtualEdition, Category category) {
		this.id = category.getNameInEditionContext(virtualEdition);
		this.text = category.getNameInEditionContext(virtualEdition);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String name) {
		this.text = name;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

}
