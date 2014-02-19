package pt.ist.socialsoftware.edition.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AltText;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.Edition.SourceType;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.GapText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.Rend;
import pt.ist.socialsoftware.edition.domain.Rend.Rendition;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.Surface;
import pt.ist.socialsoftware.edition.domain.TextPortion;
import pt.ist.socialsoftware.edition.domain.UnclearText;

public class HtmlWriter4OneInter extends HtmlWriter {
	protected FragInter fragInter = null;
	protected String transcription = "";

	protected Boolean highlightDiff = false;
	protected Boolean displayDel = false;
	protected Boolean highlightIns = true;
	protected Boolean highlightSubst = false;
	protected Boolean showNotes = true;

	private PbText startPbText = null;
	private PbText stopPbText = null;

	// by default the georgia font is used
	protected String font = "style=\"font-family:georgia;\"";

	private final Map<FragInter, Integer> interpsChar = new HashMap<FragInter, Integer>();
	private int totalChar = 0;

	public String getTranscription() {
		return transcription;
	}

	public Integer getInterPercentage(FragInter inter) {
		return (interpsChar.get(inter) * 100) / totalChar;
	}

	public HtmlWriter4OneInter(FragInter fragInter) {
		this.fragInter = fragInter;
		transcription = "";

		for (FragInter inter : fragInter.getFragment().getFragmentInterSet()) {
			interpsChar.put(inter, 0);
		}

		if (fragInter.getLastUsed().getSourceType() == SourceType.EDITORIAL) {
			font = "style=\"font-family:courier;\"";
		}
	}

	public void write(Boolean highlightDiff) {
		this.highlightDiff = highlightDiff;
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}
		visit((AppText) fragInter.getFragment().getTextPortion());
	}

	public void write(Boolean highlightDiff, Boolean displayDel,
			Boolean highlightIns, Boolean highlightSubst, Boolean showNotes,
			Surface surface) {
		this.highlightDiff = highlightDiff;
		this.displayDel = displayDel;
		this.highlightIns = highlightIns;
		this.highlightSubst = highlightSubst;
		this.showNotes = showNotes;
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}

		if (surface == null) {
			visit((AppText) fragInter.getFragment().getTextPortion());
		} else {
			Surface stopSurface = surface.getNext();
			if (stopSurface == null) {
				stopPbText = null;
			} else {
				stopPbText = stopSurface.getPbText();
			}
			startPbText = surface.getPbText();
			if (startPbText == null) {
				visit((AppText) fragInter.getFragment().getTextPortion());
			} else {
				visit(surface.getPbText());
			}
		}
	}

	@Override
	public void visit(AppText appText) {
		TextPortion firstChild = appText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (appText.getParentOfLastText() == null) {
			if (appText.getNextText() != null) {
				appText.getNextText().accept(this);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		if (rdgGrpText.getInterps().contains(this.fragInter)) {
			TextPortion firstChild = rdgGrpText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}
		}

		if (rdgGrpText.getParentOfLastText() == null) {
			rdgGrpText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RdgText rdgText) {
		if (rdgText.getInterps().contains(this.fragInter)) {

			Boolean color = false;
			if (highlightDiff) {
				int size = fragInter.getFragment().getFragmentInterSet().size();
				if (rdgText.getInterps().size() < size) {
					color = true;
					int colorValue = 255 - (255 / size)
							* (size - rdgText.getInterps().size() - 1);
					String colorCode = "<span style=\"background-color: rgb(0,"
							+ colorValue + ",255);\">";

					transcription = transcription
							+ rdgText.writeSeparator(displayDel,
									highlightSubst, fragInter) + colorCode;
				}
			}

			if (!color) {
				transcription = transcription
						+ rdgText.writeSeparator(displayDel, highlightSubst,
								fragInter);
			}

			TextPortion firstChild = rdgText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}

			if (color) {
				transcription = transcription + "</span>";
			}
		}

		if (rdgText.getParentOfLastText() == null) {
			rdgText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		transcription = transcription + "<p align=\"justify\">";

		TextPortion firstChild = paragraphText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		transcription = transcription + "</p>";

		if (paragraphText.getParentOfLastText() == null) {
			paragraphText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SegText segText) {
		List<Rend> renditions = new ArrayList<Rend>(segText.getRendSet());
		String preRend = generatePreRendition(renditions);
		String postRend = generatePostRendition(renditions);

		String altRend = "";
		if (segText.getAltTextOne() != null) {
			altRend = "<span class=\"text-warning\">" + "<abbr title=\""
					+ segText.getAltTextOne().getMode().getDesc() + " "
					+ segText.getAltTextOne().getWeightOne() + "\">";
		} else if (segText.getAltTextTwo() != null) {
			altRend = "<span class=\"text-warning\">" + "<abbr title=\""
					+ segText.getAltTextTwo().getMode().getDesc() + " "
					+ segText.getAltTextTwo().getWeightTwo() + "\">";

		}

		transcription = transcription
				+ segText.writeSeparator(displayDel, highlightSubst, fragInter)
				+ preRend + altRend;

		TextPortion firstChild = segText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if ((segText.getAltTextOne() != null)
				|| (segText.getAltTextTwo() != null)) {

		}

		if ((segText.getAltTextOne()) != null
				|| (segText.getAltTextTwo() != null)) {
			altRend = "</abbr></span>";
		}

		transcription = transcription + altRend + postRend;

		if (segText.getParentOfLastText() == null) {
			segText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AltText altText) {
		// do nothing, the segTextOne and segTextTwo will do
		if (altText.getParentOfLastText() == null) {
			altText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SimpleText simpleText) {
		String value = simpleText.getValue();

		totalChar = totalChar + value.length();
		for (FragInter inter : simpleText.getInterps()) {
			Integer number = interpsChar.get(inter);
			number = number + value.length();
			interpsChar.put(inter, number);
		}

		transcription = transcription
				+ simpleText.writeSeparator(displayDel, highlightSubst,
						fragInter) + value;

		if (simpleText.getParentOfLastText() == null) {
			simpleText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		if (lbText.getInterps().contains(fragInter)) {
			String hyphen = "";
			if (lbText.getHyphenated()) {
				hyphen = "-";
			}

			transcription = transcription + hyphen + "<br>";
		}

		if (lbText.getParentOfLastText() == null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {
		if (pbText.getInterps().contains(fragInter)) {
			if ((startPbText != pbText) && (stopPbText != pbText)) {
				transcription = transcription
						+ "<hr size=\"3\" color=\"black\">";
			}
		}

		if (stopPbText != pbText) {
			if (pbText.getParentOfLastText() == null) {
				pbText.getNextText().accept(this);
			}
		}
	}

	@Override
	public void visit(SpaceText spaceText) {
		String separator = "";
		if (spaceText.getDim() == SpaceDim.VERTICAL) {
			separator = "<br>";
			// the initial line break is for a new line
			transcription = transcription + separator;
		} else if (spaceText.getDim() == SpaceDim.HORIZONTAL) {
			separator = "&nbsp; ";
		}

		for (int i = 0; i < spaceText.getQuantity(); i++) {
			transcription = transcription + separator;
		}

		if (spaceText.getParentOfLastText() == null) {
			spaceText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		List<Rend> renditions = new ArrayList<Rend>(addText.getRendSet());
		String preRendition = generatePreRendition(renditions);
		String postRendition = generatePostRendition(renditions);

		String prePlaceFormat = "";
		String postPlaceFormat = "";
		switch (addText.getPlace()) {
		case INLINE:
		case INSPACE:
		case OVERLEAF:
		case SUPERIMPOSED:
		case MARGIN:
		case OPPOSITE:
		case BOTTOM:
		case END:
		case UNSPECIFIED:
			prePlaceFormat = "<small>";
			postPlaceFormat = "</small>";
			break;
		case ABOVE:
		case TOP:
			prePlaceFormat = "<sup>";
			postPlaceFormat = "</sup>";
			break;
		case BELOW:
			prePlaceFormat = "<sub>";
			postPlaceFormat = "</sub>";
			break;
		}

		if (highlightIns) {
			String insertSymbol = "<span style=\"color: rgb(128,128,128);\"><small>&and;</small></span>";
			if (showNotes) {
				insertSymbol = "<abbr title=\"" + addText.getNote() + "\">"
						+ insertSymbol + "</abbr>";
			}

			transcription = transcription
					+ addText.writeSeparator(displayDel, highlightSubst,
							fragInter) + preRendition + prePlaceFormat
					+ insertSymbol;
		} else {
			transcription = transcription
					+ addText.writeSeparator(displayDel, highlightSubst,
							fragInter);
		}

		TextPortion firstChild = addText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (highlightIns) {
			transcription = transcription + postPlaceFormat + postRendition;
		}

		if (addText.getParentOfLastText() == null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		if (displayDel) {
			transcription = transcription
					+ delText.writeSeparator(displayDel, highlightSubst,
							fragInter)
					+ "<del><span style=\"color: rgb(128,128,128);\">";
			if (showNotes) {
				transcription = transcription + "<abbr title=\""
						+ delText.getNote() + "\">";
			}

			TextPortion firstChild = delText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}

			if (showNotes) {
				transcription = transcription + "</abbr>";
			}

			transcription = transcription + "</span></del>";
		}

		if (delText.getParentOfLastText() == null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {
		if (displayDel && highlightSubst) {
			transcription = transcription
					+ substText.writeSeparator(displayDel, highlightSubst,
							fragInter)
					+ "<span style=\"background-color: rgb(255,255,0);\">";
		}

		TextPortion firstChild = substText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (displayDel && highlightSubst) {
			transcription = transcription + "</span>";
		}

		if (substText.getParentOfLastText() == null) {
			substText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(GapText gapText) {
		String gapValue = gapText.getGapValue();

		totalChar = totalChar + gapValue.length();
		for (FragInter inter : gapText.getInterps()) {
			Integer number = interpsChar.get(inter);
			number = number + gapValue.length();
			interpsChar.put(inter, number);
		}

		transcription = transcription
				+ gapText.writeSeparator(displayDel, highlightSubst, fragInter)
				+ "<abbr title=\"" + gapText.getReason().getDesc() + "\">"
				+ gapValue + "</abbr>";

		if (gapText.getParentOfLastText() == null) {
			gapText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(UnclearText unclearText) {
		transcription = transcription
				+ unclearText.writeSeparator(displayDel, highlightSubst,
						fragInter)
				+ "<span style=\"text-shadow: black 0.0em 0.0em 0.1em; -webkit-filter: blur(0.005em);\">"
				+ "<abbr title=\"" + unclearText.getReason().getDesc() + "\">";

		TextPortion firstChild = unclearText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		transcription = transcription + "</abbr>" + "</span>";

		if (unclearText.getParentOfLastText() == null) {
			unclearText.getNextText().accept(this);
		}

	}

	private String generatePreRendition(List<Rend> renditions) {
		String preRend = "";
		for (Rend rend : renditions) {
			// the order matters
			if (rend.getRend() == Rendition.RIGHT) {
				preRend = "<div class=\"text-right\">" + preRend;
			} else if (rend.getRend() == Rendition.LEFT) {
				preRend = "<div class=\"text-left\">" + preRend;
			} else if (rend.getRend() == Rendition.CENTER) {
				preRend = "<div class=\"text-center\">" + preRend;
			} else if (rend.getRend() == Rendition.BOLD) {
				preRend = preRend + "<strong>";
			} else if (rend.getRend() == Rendition.ITALIC) {
				preRend = preRend + "<em>";
			} else if (rend.getRend() == Rendition.RED) {
				preRend = preRend + "<span style=\"color: rgb(255,0,0);\">";
			} else if (rend.getRend() == Rendition.UNDERLINED) {
				preRend = preRend + "<u>";
			}
		}
		return preRend;
	}

	private String generatePostRendition(List<Rend> renditions) {
		String postRend = "";
		for (Rend rend : renditions) {
			if (rend.getRend() == Rendition.RIGHT) {
				postRend = postRend + "</div>";
			} else if (rend.getRend() == Rendition.LEFT) {
				postRend = postRend + "</div>";
			} else if (rend.getRend() == Rendition.CENTER) {
				postRend = postRend + "</div>";
			} else if (rend.getRend() == Rendition.BOLD) {
				postRend = "</strong>" + postRend;
			} else if (rend.getRend() == Rendition.ITALIC) {
				postRend = "</em>" + postRend;
			} else if (rend.getRend() == Rendition.RED) {
				postRend = "</span>" + postRend;
			} else if (rend.getRend() == Rendition.UNDERLINED) {
				postRend = "</u>" + postRend;
			}
		}
		return postRend;
	}

}