from sklearn.datasets import load_iris
from sklearn import tree
import matplotlib.pyplot as plt

iris = load_iris()
X, y = iris.data, iris.target               # cechy (X) i etykiety klas (y) dla każdego przykładu w zbiorze
clf = tree.DecisionTreeClassifier()
# Trenuje klasyfikator drzewa decyzyjnego na danych X i y.
# Metoda fit() dopasowuje model do danych treningowych,
# ucząc go, jak przewidywać etykiety klas na podstawie dostarczonych cech.
clf = clf.fit(X, y)
tree.plot_tree(clf)
plt.show()
