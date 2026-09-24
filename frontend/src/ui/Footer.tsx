export default function Footer() {
  return (
    <footer className="container-page mt-16 mb-6 flex justify-center border-t border-line pt-6 text-sm text-muted">
      <a
        href="https://cristianruiz.dev"
        target="_blank"
        rel="noopener noreferrer"
        className="font-medium transition-colors hover:text-accent-strong"
      >
        cristianruiz.dev 🚀 {new Date().getFullYear()}
      </a>
    </footer>
  );
}
