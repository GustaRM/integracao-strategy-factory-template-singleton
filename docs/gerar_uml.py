"""Gera o diagrama UML (estilo ferramenta de desenho, fundo quadriculado)."""
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle, FancyArrowPatch

LH, PAD, NAME_H = 0.52, 0.25, 0.75
W_UN, H_UN = 57, 31

fig, ax = plt.subplots(figsize=(22.5, 12.2))
ax.set_xlim(0, W_UN); ax.set_ylim(0, H_UN); ax.set_aspect("equal"); ax.axis("off")
for i in range(W_UN + 1):
    ax.plot([i, i], [0, H_UN], color="#E3E3E3", lw=0.6, zorder=0)
for j in range(H_UN + 1):
    ax.plot([0, W_UN], [j, j], color="#E3E3E3", lw=0.6, zorder=0)

def box(cx, top, w, name, stereo=None, attrs=(), methods=()):
    h_name = NAME_H * (2 if stereo else 1) + (0.15 if stereo else 0)
    h_attr = max(len(attrs), 1) * LH + PAD
    h_meth = max(len(methods), 1) * LH + PAD
    h = h_name + h_attr + h_meth
    x0, y0 = cx - w / 2, top - h
    ax.add_patch(Rectangle((x0, y0), w, h, fc="white", ec="black", lw=1.4, zorder=2))
    ax.plot([x0, x0 + w], [top - h_name] * 2, color="black", lw=1.2, zorder=3)
    ax.plot([x0, x0 + w], [top - h_name - h_attr] * 2, color="black", lw=1.2, zorder=3)
    ty = top - 0.45
    if stereo:
        ax.text(cx, ty, f"«{stereo}»", ha="center", va="center", fontsize=9, zorder=4)
        ty -= 0.65
    ax.text(cx, ty, name, ha="center", va="center", fontsize=11, fontweight="bold", zorder=4)
    for k, a in enumerate(attrs):
        ax.text(x0 + 0.25, top - h_name - PAD / 2 - LH * (k + .5), a, ha="left", va="center", fontsize=8.5, zorder=4)
    for k, m in enumerate(methods):
        ax.text(x0 + 0.25, top - h_name - h_attr - PAD / 2 - LH * (k + .5), m, ha="left", va="center", fontsize=8.5, zorder=4)
    return dict(cx=cx, cy=(top + y0) / 2, top=top, bottom=y0, left=x0, right=x0 + w)

def line(pts, dashed=False):
    xs, ys = zip(*pts)
    ax.plot(xs, ys, color="black", lw=1.3, zorder=1, linestyle=(0, (5, 4)) if dashed else "-")

def arrow(p0, p1, style, dashed=False):
    a = FancyArrowPatch(p0, p1, arrowstyle=style, mutation_scale=22 if "|" in style else 18, lw=1.3,
                        color="black", zorder=1, shrinkA=0, shrinkB=0,
                        linestyle=(0, (5, 4)) if dashed else "-")
    if "|" in style:
        a.set_facecolor("white")
    ax.add_patch(a)

# ---------------- classes ----------------
main = box(8, 30.0, 5.6, "Main", None, (), ("+ main(args : String[])",))

fab = box(8, 26.0, 14, "FabricaGuias", None,
          ("- instance : FabricaGuias  {static}",),
          ("+ getInstance() : FabricaGuias  {static}",
           "+ criar(tipo : TipoTributo) : GuiaTributo"))
enum = box(8, 19.8, 7.6, "TipoTributo", "enumeration", ("ISS", "ICMS", "IRPF"), ())

guia = box(32, 28.0, 18.5, "GuiaTributo", "abstract",
           ("- contribuinte : String", "- baseCalculo : double", "- valorImposto : double"),
           ("+ emitir(contribuinte, baseCalculo) : String  {final, template}",
            "- validar(contribuinte, baseCalculo)",
            "# criarCalculo() : CalculoTributo  {abstract, factory method}",
            "# getNomeTributo() : String  {abstract}",
            "# getPrazoVencimentoDias() : int  {hook}",
            "+ getters"))

g_iss = box(21.5, 16.8, 9.4, "GuiaIss", None, (),
            ("# criarCalculo() : CalculoTributo", "# getNomeTributo() : String"))
g_icms = box(32, 16.8, 9.4, "GuiaIcms", None, (),
             ("# criarCalculo() : CalculoTributo", "# getNomeTributo() : String",
              "# getPrazoVencimentoDias() : int"))
g_irpf = box(42.5, 16.8, 9.4, "GuiaIrpf", None, (),
             ("# criarCalculo() : CalculoTributo", "# getNomeTributo() : String"))
guias = [g_iss, g_icms, g_irpf]

c_iss = box(21.5, 10.4, 9.4, "CalculoIss", None, ("- ALIQUOTA = 0.05  {static}",),
            ("+ calcular(baseCalculo) : double",))
c_icms = box(32, 10.4, 9.4, "CalculoIcms", None, ("- ALIQUOTA = 0.18  {static}",),
             ("+ calcular(baseCalculo) : double",))
c_irpf = box(42.5, 10.4, 9.4, "CalculoIrpf", None, (),
             ("+ calcular(baseCalculo) : double",))
calcs = [c_iss, c_icms, c_irpf]

iface = box(32, 5.4, 11.4, "CalculoTributo", "interface", (),
            ("+ calcular(baseCalculo : double) : double",))

# ---------------- Singleton / Factory ----------------
arrow((8, main["bottom"]), (8, fab["top"]), "->", dashed=True)                 # Main -> Fabrica
arrow((8, fab["bottom"]), (8, enum["top"]), "->", dashed=True)                 # Fabrica -> TipoTributo
yf = fab["top"] - 2.3
arrow((fab["right"], yf), (guia["left"], yf), "->", dashed=True)               # Fabrica -> GuiaTributo (cria)

# autoassociação (Singleton): Fabrica -> Fabrica, "1"
lx = 8 + 4.5
line([(lx, fab["top"]), (lx, fab["top"] + 1.2), (fab["right"] + 1.5, fab["top"] + 1.2),
      (fab["right"] + 1.5, fab["top"] - 1.5)])
arrow((fab["right"] + 1.5, fab["top"] - 1.5), (fab["right"], fab["top"] - 1.5), "->")
ax.text(fab["right"] + 0.15, fab["top"] - 1.15, "1", fontsize=9)

# ---------------- Template Method: herança ----------------
ymid = (guia["bottom"] + g_iss["top"]) / 2
for g in guias:
    line([(g["cx"], g["top"]), (g["cx"], ymid)])
line([(g_iss["cx"], ymid), (g_irpf["cx"], ymid)])
arrow((32, ymid), (32, guia["bottom"]), "-|>")

# ---------------- Factory Method: Guia -> Calculo (cria) ----------------
for g, c in zip(guias, calcs):
    arrow((g["cx"], g["bottom"]), (c["cx"], c["top"]), "->", dashed=True)

# ---------------- Strategy: realização ----------------
ymid2 = (c_iss["bottom"] + iface["top"]) / 2
for c in calcs:
    line([(c["cx"], c["bottom"]), (c["cx"], ymid2)], dashed=True)
line([(c_iss["cx"], ymid2), (c_irpf["cx"], ymid2)], dashed=True)
arrow((32, ymid2), (32, iface["top"]), "-|>", dashed=True)

# GuiaTributo -> CalculoTributo (usa)
yu = guia["top"] - 1.6
xr = 50.5
line([(guia["right"], yu), (xr, yu), (xr, iface["cy"]), (iface["right"] + 0.9, iface["cy"])], dashed=True)
arrow((iface["right"] + 0.9, iface["cy"]), (iface["right"], iface["cy"]), "->", dashed=True)

# legenda dos padrões
leg = [("Singleton", "FabricaGuias"), ("Factory", "FabricaGuias.criar() e GuiaTributo.criarCalculo()"),
       ("Template Method", "GuiaTributo.emitir()"), ("Strategy", "CalculoTributo e implementações")]
ax.text(1.0, 12.4, "Padrões e onde aparecem", fontsize=9.5, fontweight="bold")
for k, (p, d) in enumerate(leg):
    ax.text(1.0, 11.5 - k * 0.8, f"• {p}: {d}", fontsize=8.5)

plt.savefig("diagrama-uml.png", dpi=140, bbox_inches="tight", facecolor="white")
