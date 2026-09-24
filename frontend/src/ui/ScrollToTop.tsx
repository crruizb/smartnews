import { useLayoutEffect } from "react";
import { useLocation, useNavigationType } from "react-router";

/**
 * Resets scroll position when navigating to a new page, so an article opened
 * from a scrolled feed starts at the top. Back/forward ("POP") navigations are
 * left to the browser.
 */
export default function ScrollToTop() {
  const { pathname } = useLocation();
  const navigationType = useNavigationType();

  // useLayoutEffect so the jump happens before paint (no visible flash).
  useLayoutEffect(() => {
    if (navigationType === "POP") return;
    try {
      window.scrollTo({ top: 0, left: 0, behavior: "instant" });
    } catch {
      window.scrollTo(0, 0);
    }
  }, [pathname, navigationType]);

  return null;
}
