package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class VirtualEditionInterListDto {
	List<VirtualEditionInterDto> virtualEditionInterList = new ArrayList<>();

	public VirtualEditionInterListDto() {
	}

	public VirtualEditionInterListDto(VirtualEdition virtualEdition) {
		this.virtualEditionInterList = virtualEdition.getIntersSet().stream()
				.sorted((i1, i2) -> i2.getNumber() - i1.getNumber())
				.map(i -> new VirtualEditionInterDto(i.getTitle(), i.getNumber(), i.getUrlId()))
				.collect(Collectors.toList());
	}

}
